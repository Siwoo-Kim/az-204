package com.siwoo.model;

import com.microsoft.azure.storage.table.TableServiceEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Delegate;

import java.util.Date;

/**
 * TableServiceEntity => 테이블의 엔티티의 프로퍼티 이름은 대문자로 시작해야 한다.
 * <p>
 * PartitionKey (파티셔닝), Row Key (PK) 을 지정하기. 1. 프록시를 만든다. 2. PartitionKey 에 그룹화할 프러퍼티, RowKey 에 PK 로 사용할 프러퍼티를 지정한다.
 */
@Getter
@Setter
@ToString
public class ToDo {
    private String id;
    private String type;
    private String content;
    private Boolean completed;
    private Date finishDate;
    private Date due;

    public void setCompleted(Boolean completed) {
        this.completed = completed;
        if (completed)
            finishDate = new Date();
        else
            finishDate = null;
    }

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
