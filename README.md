#AZ-203 (AZ-204)
클라우드 솔루션, 디자인을 위한 애져 개발자을 위한 자격증.

# 토픽
1. Azure Compute (IaaS)
2. Azure Compute (PaaS)
3. Azure Storage 
4. Azure Security
5. Montior, troubleshoot and optimize solutions
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