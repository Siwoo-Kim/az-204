#AZ-203 (AZ-204)
클라우드 솔루션, 디자인을 위한 애져 개발자을 위한 자격증.

# 토픽
1. Azure Compute (IaaS)
2. Azure Compute (PaaS)
3. Azure Storage 
4. Azure Security
5. Monitor, troubleshoot and optimize solutions
6. Connect Azure Services with third-party services.

# 시작전
* Azure CLI 설치.
* Storage Explorer 설치.
* 인텔리제이 Azure 플러그인

# Reference
https://github.com/Azure-Samples?language=java
https://azurecitadel.com

# Marketplace
IaaS, PaaS 제품군 리스팅

# Azure Fundamentals.
* 클라우드 기반의 서비스를 제공. (인프라 혹은 플랫폼을 Azure 데이터 센터에서 운영)
* Account 을 기반으로 애져에 접근.
* Subscription 을 이용하여 사용자에게 Resource 의 접근 권한을 제공.
* Resource 은 애져 서비스에서 소비 가능한 자원. eg) VM, Networking, App Services, Sql Server.. 

> * IaaS = 개발자에게 물리적 리소스만 이용하여 앱을 개발할 수 있도록 서비스 제공. = VM
> * PaaS = 클라우더는 개발자가 플랫폼 위에서(OS, Framework) 앱을 개발할 수 있는 서비스 제공. = MySql Server, Web Service

##Azure Fundamentals - 구독 (Subscription)
Subscription 을 이용하여 사용자에게 Resource 의 접근 권한을 제공.
하나의 Account 에 여러 Subscriptions 보유할 수 있음.

> * Azure free account
> * Pay-As-You-Go
> * Enterprise Agreements.

##Azure Fundamentals. - Resources & Resource Manager
Azure 에서 관리 가능한 모든 자원 아이템.
모든 자원 아이템은 특정 Subscription 에 등록되어야 . <br>

<code>
az group create -l $location -n $resource_group
az resource list -n $vm1 -o table
</code>

Scope 
Azure 은 4 가지 레벨의 스코프을 제공.

1. Management groups ?
2. Subscriptions
3. Resource Groups
4. Resources

##Resource Manager.
> * 리소스를 그룹화.
> * 배포 템플릿 빌드.
> * 일관성있는 관리 레이어와 툴링 제공.
> * Role 기반 access controls 제공.
> *비용을 그룹화.

Azure Resource Manager Template
> 복수의 리소스 인스턴스 생성시 템플릿을 사용.
> copy element & copyIndex for resource naming
> https://github.com/Azure/azure-quickstart-templates


<code>
az group deployment create -g $resource_group --template-file ./az-template-spark.json --parameters @./az-template-spark-params.json
</code>

##Azure Fundamentals. - Placing resources in Azure Regions
Region 은 앱이 배포될 수 있는 애져 데이터 센터 집합을 의미.
리소스 생성시 Region 을 선택해야 됨. <br>

지역 선택시 고려 사항,
> * Services Availability (해당 지역에 서비스 가용한가?)
> * Redundancy (성능 향상, failover 을 위해 해당 서비스를 복제하여 다른 지역으로 복제 가능한가?)
> * Latency (지연 시간이 허용 가능한 수치인가?)
> * Producer / Consumer (이벤트 생성자와 소비자가 서로 가까이에 위치하는가?)

# VM
스케일 가능한 컴퓨팅 리소스.
이미 인스턴스화 된 VM 을 이미지화하여 재사용 가능. <br>

VM 구성요소.
1. Image (OS and App installed)
2. Networking (public? private?)
3. Storage

- Deallocate : 가상 머신을 하드 디스크에서 완전히 해제함.
- Deprovision (waagent) : 가상 머신을 일반화 시킴 (기계의 정보, 유저 정보 제거). 이미지 생성시 사용.
- Managed Disks : 스토리지 어카운트 없이 생성된 디스크 (vhd)
- Snapshots : 읽기 전용 Managed Disks 의 카피.
- Image : 일반화된 (Generalized) VM 인스턴스
 
##VM - Provisioning VM
Based 이미지를 이용해 가상 머신을 생성. (IaasS 서비스)

<code>az vm create</code>

##VM - Building and Deploying VM Images
이미지: 미리 설치된 OS, 앱과 서비스 패키지. <br/>
커스텀 이미지: 개발자가 개발에 필요한 앱과 서비스를 미리 설치환 패키지. <br/>
커스텀 이미지를 이용해 다른 vm 생성시 사용할 수 있음. <br/>
커시텀 이미지는 Region 별로 독립적이라 다른 Region 에서 사용시 copy 해야 됨. <br/> 

커스텀 이미지 생성 방법.
1. 다른 이미지로부터 생성 이후 앱을 설치한 뒤 해당 VM 에서 Deprovision  (waagent) 실행.
2. 가상 하드 디스크를 로컬 VM 으로부터 업로드하여 생성. 
 
# Azure Storage

##Shared Access Signature (SAS)
* 스토리지의 리소스에 대한 access 권한을 제공 방법. (날짜 기반)
* 생성된 access 의 유효 날짜 (시작, 끝) 기능 제공.
* access 은 connection string, sas token, service sas url 로 생성.
 

## Azure Table Storage
* 클라우드 기반 NoSql 데이터베이스 (No Schema = key & value storage).
* JSON, XML 을 이용한 데이터 직렬화, 역직렬화.
* 유용한 SCHEMA 변경.
* Region 선택 가능. (Primary region - read & write, Secondary region (Optional) - read only)

```
az storage table --help
```

> Table Storage vs Cosmos DB
> * Table Storage                   &    Comsmos DB 
> * pk look up only                 & secondary index support
> * price opt for cold tier         & price opt for hot storage
> * lower performance               & better performance 
> * geo replication restricted      & geo replication not restricted
> * cheap                           & expensive

### Azure Table Storage - Partitioning schemas
* Table 은 복수의 파티션 키를 가진다.
* 파티션 키는 데이터를 그룹하는 데 사용. (group by partition key)
* 쿼리 성능 개선을 위해 사용. => partition key 와 row key 을 제공해야 원하는 데이터를 쿼리 가능.
* 파티션 키를 이용해 데이터의 물리적 저장 위치가 결정. (스파크 parquet 와 비슷)

### Azure Table Storage -  Migration from Table Storage to Cosmos DB
https://github.com/azure/azure-documentdb-datamigrationtool

### Azure Table Storage - Applying and Enforcing Table Policies
테이블에 특정 클라이언트 (third-party) 에게 접근 허용하는 정책 수립 가능.
이후 Azure Storage API 을 이용해 지정한 "identifier" 을 통해 SAS 토근 생성.

1. 테이블에서 Policy 추가.
2. Policy 에 대한 identifier 추가.
3. 퍼미션 (읽기, 쓰기, 업데이트, 삭제) 추가.
4. start & end time 추가.

### Azure Table Storage - Exporting Metrics
Metrics -> Share -> Export as csv <br>
혹은 메트릭들 $Metric 테이블에 모두 저장됨.

* Table Capacity (테이블 수용력)
* Table Entity Count (로우 갯수)
* Success E2E Latency (End to End 지연시간)

## Azure Cosmos DB
```
az cosmosdb
```

### Securing Data (데이터 보호하기)
1. Managed Service Identity
2. Azure SQL Database
3. Azure Storage Service Encryption
4. Azure Disk Encryption
5. SSL/TLS

### Azure Policy and Azure RBAC (애져 리소스에 대한 정책 및 규칙 정의 - 보안 정책이 아님)
RBAC (Role based access control)
- Focuses on what resources the users can access
- 특정 리소스에 대해 유저가 접근, 혹은 어떠한 동작이 허용되는지를 지정.

Azure Policy
- Focuses on the properties of resources during deployment.
- 유저가 리소스를 배포시에 적용되는 정책 
- Builtin Azure Policy - Allowed locations (특정 region 에 배포), 
VM SKUs (특정 지정된 size 의 VM 생성만 허용), Deploy MMS extension
- Assignments - creating & assign policy
- Definitions - collection of policies