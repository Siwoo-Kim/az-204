az storage account create -n $storage_account -g $resource_group -l $location --kin StorageV2 --access-tier hot
#Storage Account 생성
connection_string=$(az storage account show-connection-string -n $storage_account -o tsv --query connectionString)
az storage table create -n todo --account-name $storage_account --connection-string $connection_string
#테이블 생성
az storage table list --account-name $storage_account -o table

#Migration from table storage to cosmosdb
az cosmosdb create -n $cosmos_account --locations regionName=$location --capabilities EnableTable -g $resource_group
#cosmoms db 생성
#https://github.com/azure/azure-documentdb-datamigrationtool 툴을 이용한 migration

