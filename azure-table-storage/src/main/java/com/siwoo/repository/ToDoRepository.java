package com.siwoo.repository;

import com.google.common.collect.ImmutableMap;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.table.*;
import com.siwoo.model.ToDo;
import com.siwoo.util.AppProperties;

import java.security.InvalidKeyException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.TemporalField;
import java.util.*;

/**
 * Azure Table Storage Component 1. CloudStorageAccount 2. CloudTableClient -> 복수의 테이블에 대한 action 수행. 3. CloudTable ->
 * 특정 테이블에 대한 action 수행. 4. TableEntity, TableServiceEntity -> 테이블에 저장된 데이터 표현 클래스 (Entity) 5. TableQuery -> 쿼리 오브젝트.
 * (TableQuery.from) 6. TableQuerySegment -> 쿼리된 데이터의 부분 집합. (페이징 기능) 7. TableOperation  -> insert, update, merge,
 * delete, replace or retrieve table entities
 * <p>
 * Table Storage 특징. 1. 클래스 정의에 따른 프로퍼티가 없다 테이블에 새로 만들어진다.
 * <p>
 * Query Component 1. TableQuery - 쿼리 표현 객체. 2. TableQuery.QueryComparisons - 쿼리 where 절 조건시 비교 연산 (eq, neq, gt, lt) 3.
 * TableOperation - update, insert, merge, delete 연산
 */
public class ToDoRepository {
    private final AppProperties appProperties = AppProperties.INSTANCE;
    private final String tableName = "todo";
    private final CloudStorageAccount storageAccount;
    private final CloudTableClient tableClient;
    private final CloudTable table;
    private String SAS_TOKEN;

    public ToDoRepository() {
        try {
            storageAccount = CloudStorageAccount.parse(appProperties.get("azure.storage.connection_string"));
            tableClient = storageAccount.createCloudTableClient();
            table = tableClient.getTableReference(tableName);
            initSAS();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot connect to the storage.");
        }
    }

    /**
     * SAS 토큰 생성하기. (API 접근 허)
     */
    private void initSAS() throws StorageException, InvalidKeyException {
        Date start = toDate(LocalDateTime.now().minusDays(1));
        Date end = toDate(LocalDateTime.now().plusDays(1));
        //명시적 SAS 토근 생성
//        SharedAccessTablePolicy policy = new SharedAccessTablePolicy();
//        policy.setPermissions(EnumSet.of(SharedAccessTablePermissions.QUERY));
//        policy.setSharedAccessStartTime(start);
//        policy.setSharedAccessExpiryTime(end);
        //테이블에 수립된 Policy 을 이용한 SAS 토큰 생성.
        SAS_TOKEN = table.generateSharedAccessSignature(null, "siwoopolicy", null, null, null, null);
        System.out.println(SAS_TOKEN);
    }

    private Date toDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 1. Making a query 2. execute on the query
     *
     * @return
     */
    public List<ToDo> getAll() {
        TableQuery<ToDo.AzureToDo> query = TableQuery.from(ToDo.AzureToDo.class);
        Iterable<ToDo.AzureToDo> iterable = table.execute(query);
        return toList(iterable);
    }

    public ToDo getByTypeAndId(String type, String id) {
        try {
            TableOperation op = TableOperation.retrieve(type, id, ToDo.AzureToDo.class);
            TableResult result = table.execute(op);
            // properties & session
            return ToDo.AzureToDo.class.cast(result.getResult()).toModel();
        } catch (StorageException e) {
            return null;
        }
    }

    public List<ToDo> getByCompleted(boolean completion) {
        TableQuery<ToDo.AzureToDo> query = TableQuery.from(ToDo.AzureToDo.class)
                .where(TableQuery.generateFilterCondition(
                        "Completed", TableQuery.QueryComparisons.EQUAL, completion));
        Iterable<ToDo.AzureToDo> iterable = table.execute(query);
        return toList(iterable);
    }

    //Bug
    public List<ToDo> getBy(Map<String, String> parameters) {
        TableQuery<ToDo.AzureToDo> query = TableQuery.from(ToDo.AzureToDo.class);
        String f = TableQuery.generateFilterCondition("1",
                TableQuery.QueryComparisons.EQUAL, 1);
        for (String p : parameters.keySet()) {
            String filter = TableQuery.generateFilterCondition(p,
                    TableQuery.QueryComparisons.EQUAL, parameters.get(p));
            f = TableQuery.combineFilters(f, TableQuery.Operators.AND, filter);
        }
        return toList(table.execute(query));
    }

    public boolean save(ToDo toDo) {
        assert toDo != null;
        try {
            TableOperation op = TableOperation.insertOrMerge(toDo.toAzure());
            table.execute(op);
        } catch (StorageException e) {
            return false;
        }
        return true;
    }

    public boolean delete(ToDo toDo) {
        assert toDo != null;
        try {
            TableOperation op = TableOperation.delete(toDo.toAzure());
            table.execute(op);
        } catch (StorageException e) {
            return false;
        }
        return true;
    }

    private List<ToDo> toList(Iterable<ToDo.AzureToDo> iterable) {
        List<ToDo> result = new ArrayList<>();
        for (ToDo.AzureToDo todo : iterable) {
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
        toDos = repository.getByCompleted(false);
        System.out.println(toDos);

        ToDo toDo = new ToDo();
        toDo.setId("5");
        toDo.setType("Vacation");
        toDo.setCompleted(false);
        toDo.setContent("Test Content");
        toDo.setDue(new Date());
        repository.save(toDo);
        toDos = repository.getByCompleted(false);    //bug on completed
        System.out.println(toDos);

        toDo = new ToDo();
        toDo.setId("6");
        toDo.setType("Work");
        toDo.setCompleted(false);
        toDo.setContent("Fix the bug");
        toDo.setDue(new Date());
        repository.save(toDo);
        System.out.println(repository.getAll());

        System.out.println(repository.getByTypeAndId(toDo.getType(), toDo.getId()));

        System.out.println("==============");
        System.out.println(repository.getBy(ImmutableMap.of("Content", "Fix the bug", "PartitionKey", "Work")));

        toDo.setCompleted(true);
        repository.save(toDo);
        System.out.println(repository.getByTypeAndId(toDo.getType(), toDo.getId()));
    }
}
