package com.siwoo.model;

import com.microsoft.azure.storage.table.TableServiceEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Delegate;
import java.util.Date;

/**
 * TableServiceEntity
 *  => 테이블의 엔티티의 프로퍼티 이름은 대문자로 시작해야 한다.
 *
 */
@Getter @Setter @ToString
public class ToDo {
    private String id;
    private String type;
    private String content;
    private Boolean completed;
    private String due;

    @ToString
    public static class AzureToDo extends TableServiceEntity {
        @Delegate
        private final ToDo target;
        private Date timestamp;     //inserted time

        public AzureToDo() {
            this.target = new ToDo();
        }

        public AzureToDo(ToDo target) {
            this.target = target;
        }

        public ToDo toModel() {
            return target;
        }

        @Override
        public String getPartitionKey() {
            return target.type;
        }

        @Override
        public String getRowKey() {
            return target.id;
        }

        @Override
        public void setPartitionKey(String partitionKey) {
            target.setType(partitionKey);
        }

        @Override
        public void setRowKey(String rowKey) {
            target.setId(rowKey);
        }
    }

    public AzureToDo toAzure() {
        return new AzureToDo(this);
    }
}
