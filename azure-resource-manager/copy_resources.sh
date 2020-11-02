#https://github.com/Azure/azure-quickstart-templates

#VM with Managed DISK - https://github.com/Azure/azure-quickstart-templates/tree/master/201-vm-copy-managed-disks

az group deployment create -g $resource_group --template-file ./az-template-spark.json --parameters @./az-template-spark-params.json
