#!/bin/bash

#SSH 로 원격 접속
ssh $admin_username@$pip

# deprovision
sudo waagent -deprovision+user -force
exit

#가상 머신 deallocate
az vm deallocate -g $resource_group -n $vm1
#가상 머신 상태 확인
az vm list --show-details -o table
#가상 머신 일반화 (이미지를 위해)
az vm generalize -g $resource_group -n $vm1
#가상 머신로 부터 이미지 생성
az image create -g $resource_group -n $image1 --source $vm1
#이미지 리스팅
az image list -o table

#이미지로부터 vm 생성
az vm create -n $vm2 \
  -g $resource_group \
  -l $location \
  --image $image1 \
  --admin-username $admin_username \
  --authentication-type ssh \
  --ssh-key-values ~/.ssh/id_rsa.pub

#다른 Region 으로 이미지 복제하기.
az extension add --name image-copy-extension
az image copy --source-object-name $image1 \
  --source-resource-group $resource_group \
  --target-location $location2 \
  --target-resource-group $resource_group2 \
  --target-name $image2 --cleanup
