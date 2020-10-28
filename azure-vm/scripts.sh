#!/bin/bash
az group create -l $location -n $resource_group
#리소스 그룹 생성
az group list -o table
#리소스 그룹 리스팅

az network vnet create --address-prefixes 10.0.0.0/16 \
  -n $vnet \
  -g $resource_group \
  --subnet-name $subnet \
  --subnet-prefixes 10.0.1.0/24
#가상 네트워 생성 /10.0.0.0 - 10.0.255.255/ $subnet = /10.0.1.0 - 10.0.1.255/

az network vnet list -o table
#vnet 리스팅

az network public-ip create -g $resource_group -n $pip1
#public ip 생성

az network public-ip list -o table
#public ip 리스팅

az network nsg create -g $resource_group -n $nsg1
#네트워크 시큐리티 그룹 생성

az network nic create -g $resource_group  \
  -n $nic1 \
  --vnet-name $vnet \
   --subnet $subnet  \
   --network-security-group $nsg1 \
   --public-ip-address $pip1
#가상 네트워크 인터페이스 생성

az network nic list -o table
#가상 네트워크 인터페이스 리스

az vm create -n $vm1 \
  -g $resource_group  \
  -l $location  \
  --image ubuntults  \
  --size Standard_B1ms \
  --nics $nic1  \
  --admin-username $admin_username  \
  --admin-password $admin_pw  \
  --authentication-type ssh \
  --ssh-key-values ~/.ssh/id_rsa.pub
#VM 생성

az vm create -n $vm1 \
  -g $resource_group  \
  -l $location  \
  --image ubuntults  \
  --size Standard_B1ms \
  --admin-username $admin_username  \
  --authentication-type ssh \
  --ssh-key-values ~/.ssh/id_rsa.pub
#VM 생성, 간단 버전

az vm list-ip-addresses -o table
#VM 아이피 확인

az vm open-port -g $resource_group -n $vm1 --port 22
#ssh 포트 개방

ssh -l $admin_username $public_ip

#ssh 접속
sudo apt update
apt install openjdk-8-jre-headless
sudo apt install maven
#앱 설치 ..

#VM 상태 확인
az vm show -g $resource_group -n $vm1
az vm get-instance-view -n $vm1 -g $resource_group -o table

#VM 끄기
az vm stop -g $resource_group -n $vm1
# 이 상태에선 하드웨어가 provisioning 되어, 계속 billing 됨.
az vm deallocate -g $resource_group -n $vm1

az vm start -g $resource_group -n $vm1

az vm delete -g $resource_group -n $vm1
# VM 삭