az policy assignment create
--display-name mydisplay
--name myPolicy --policy {PolicyName}
--scope "/providers/Microsoft.Management/managementGroups/MyManagementGroup"

az policy assignment create --policy {PolicyName} -p "{ \"allowedLocations\": \
      { \"value\": [ \"australiaeast\", \"eastus\", \"japaneast\" ] } }"

# Valid scopes are management group, subscription, resource group, and resource, for example
# management group:  /providers/Microsoft.Management/managementGroups/MyManagementGroup
#           subscription:      /subscriptions/0b1f6471-1bf0-4dda-aec3-111122223333
#           resource group:    /subscriptions/0b1f6471-1bf0-4dda-aec3-111122223333/resourceGroups/myGroup
#           resource:          /subscriptions/0b1f6471-1bf0-4dda-aec3-111122223333/resourceGroups/myGroup/providers/Microsoft.Compute/virtualMachines/myVM
# 그냥 azure policy 포탈에서 설정하는게 편함.. builtin Policy 가 너무 많음
