package com.siwoo.repository;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.CloudTableClient;
import com.microsoft.azure.storage.table.TableOperation;
import com.microsoft.azure.storage.table.TableQuery;
import com.siwoo.model.ToDo;
import com.siwoo.util.AppProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Azure Table Storage Component
 *  1. CloudStorageAccount
 *  2. CloudTableClient -> 복수의 테이블에 대한 action 수행.
 *  3. CloudTable -> 특정 테이블에 대한 action 수행.
 *  4. TableEntity, TableServiceEntity -> 테이블에 저장된 데이터 표현 클래스 (Entity)
 *  5. TableQuery -> 쿼리 오브젝트. (TableQuery.from)
 *  6. TableQuerySegment -> 쿼리된 데이터의 부분 집합. (페이징 기능)
 *  7. TableOperation  -> insert, update, merge, delete, replace or retrieve table entities
 */
public class ToDoRepository {
    private final AppProperties appProperties = AppProperties.INSTANCE;
    private final String tableName = "todo";
    private final CloudStorageAccount storageAccount;
    private final CloudTableClient tableClient;
    private final CloudTable table;

    public ToDoRepository() {
        try {
            storageAccount = CloudStorageAccount.parse(appProperties.get("azure.storage.connection_string"));
            tableClient = storageAccount.createCloudTableClient();
            table = tableClient.getTableReference(tableName);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot connect to the storage.");
        }
    }

    public List<ToDo> getAll() {
        TableQuery<ToDo.AzureToDo> query = TableQuery.from(ToDo.AzureToDo.class);
        return toList(table.execute(query));
    }

    public List<ToDo> getUncompleted() {
        TableQuery<ToDo.AzureToDo> query = TableQuery.from(ToDo.AzureToDo.class)
                .where(TableQuery.generateFilterCondition(
                        "Completed", TableQuery.QueryComparisons.EQUAL, Boolean.FALSE));
        return toList(table.execute(query));
    }

    public boolean save(ToDo toDo) {
        assert toDo != null;
        try {
            TableOperation op = TableOperation.insert(toDo.toAzure());
            table.execute(op);
        } catch (StorageException e) {
            return false;
        }
        return true;
    }

    private List<ToDo> toList(Iterable<ToDo.AzureToDo> iterable) {
        List<ToDo> result = new ArrayList<>();
        for (ToDo.AzureToDo todo: iterable) {
            //System.out.println(todo);
            result.add(todo.toModel());
        }
        return result;
    }

    public static void main(String[] args) {
        ToDoRepository repository = new ToDoRepository();
        assert repository.storageAccount != null;
        System.out.println(repository.table.getName());
        List<ToDo> toDos = repository.getAll();
        System.out.println(toDos);
        toDos = repository.getUncompleted();
        System.out.println(toDos);

        ToDo toDo = new ToDo();
        toDo.setId("5");
        toDo.setType("Vacation");
        toDo.setCompleted(false);
        toDo.setContent("Test Content");
        toDo.setDue("Two days laster");
        repository.save(toDo);
        toDos = repository.getUncompleted();    //bug on completed
        System.out.println(toDos);
    }
}
