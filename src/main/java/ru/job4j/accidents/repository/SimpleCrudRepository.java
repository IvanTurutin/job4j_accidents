package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.jcip.annotations.ThreadSafe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@ThreadSafe
@Repository
@AllArgsConstructor
@Slf4j
public class SimpleCrudRepository {
    private final SessionFactory sf;
    private static final String LOG_MESSAGE = "Exception in SimpleCrudRepository";

    /**
     * Обрабатывает JPA команды без параметров (такие как: обновить)
     * @param command сессия подключения с базой данных с примененной в ней командой действия над передаваемым объектом
     * @return true при удачном выполнении команды, false при неудачном выполнении команды
     */
    public boolean run(Consumer<Session> command) {
        boolean rslt = false;
        try {
            tx(session -> {
                        command.accept(session);
                        return null;
                    }
            );
            rslt = true;
        } catch (Exception e) {
            log.error(LOG_MESSAGE, e);
        }
        return rslt;
    }

    /**
     * Позволяет выполнять запросы HQL/SQL с параметрами
     * @param query шаблон запроса HQL/SQL
     * @param args параметры запроса для формирования запроса к БД
     */
    public void run(String query, Map<String, Object> args) {
        Consumer<Session> command = session -> {
            var sq = session
                    .createQuery(query);
            for (Map.Entry<String, Object> arg : args.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            sq.executeUpdate();
        };
        run(command);
    }

    /**
     * Добавляет объект в БД
     * @param model добавляемый объект
     * @return сохраненный объект обернутый в Optional если сохранение успешное, или Optional.empty если сохранение
     * не удалось
     * @param <T> тип сохраняемого объекта
     */
    public <T> Optional<T> add(T model) {
        return run(session -> session.save(model))
                ? Optional.of(model)
                : Optional.empty();
    }

    /**
     * Удаляет объект из БД и возвращает
     * @param query запрос к базе данных
     * @param args аргументы для запроса
     * @param model объект, который требуется удалить
     * @return удаленный объект обернутый в Optional если удаление успешное, или Optional.empty если удаление
     * не удалось
     * @param <T> тип удаляемого объекта
     */
    public <T> Optional<T> delete(String query, Map<String, Object> args, T model) {
        return query(query, args)
                ? Optional.of(model)
                : Optional.empty();
    }

    /**
     * Позволяет выполнять запросы HQL/SQL с параметрами, которые предполагают извлечение из базы данных одного объекта
     * @param query шаблон запроса HQL/SQL
     * @param cl имя класса в тип которого необходимо собрать объект
     * @param args параметры запроса для формирования запроса к БД
     * @return Optional сформированного объекта
     * @param <T> тип, в который собирается объект
     */
    public <T> Optional<T> optional(String query, Class<T> cl, Map<String, Object> args) {
        Function<Session, Optional<T>> command = session -> {
            var sq = session
                    .createQuery(query, cl);
            for (Map.Entry<String, Object> arg : args.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            var rslt = sq.getSingleResult();
            /*
             * вывод результата запроса необходим для подгрузки всех lazy объектов
             */
            System.out.println(rslt);
            return Optional.of(rslt);
        };
        try {
            return tx(command);
        } catch (Exception e) {
            log.error(LOG_MESSAGE, e);
        }
        return Optional.empty();
    }

    /**
     * Позволяет выполнять запросы HQL/SQL без параметров, которые предполагают извлечение из базы данных группу объектов
     * @param query шаблон запроса HQL/SQL
     * @param cl имя класса в тип которого необходимо собрать объект
     * @return список объектов удовлетворяющих запросу
     * @param <T> тип, в который собирается объект
     */
    public <T> List<T> query(String query, Class<T> cl) {
        Function<Session, List<T>> command = session -> {

            var sq = session
                    .createQuery(query, cl);
            var rslt = sq.list();
            rslt.forEach(System.out::println);
            return rslt;
        };
        try {
            return tx(command);
        } catch (Exception e) {
            log.error(LOG_MESSAGE, e);
        }
        return new ArrayList<>();
    }

    /**
     * Позволяет выполнять запросы HQL/SQL с параметрами, которые предполагают извлечение из базы данных группы объектов
     * @param query шаблон запроса HQL/SQL
     * @param cl имя класса в тип которого необходимо собрать объект
     * @param args параметры запроса для формирования запроса к БД
     * @return список объектов удовлетворяющих запросу
     * @param <T> тип, в который собирается объект
     */
    public <T> List<T> query(String query, Class<T> cl, Map<String, Object> args) {
        Function<Session, List<T>> command = session -> {
            var sq = session
                    .createQuery(query, cl);
            for (Map.Entry<String, Object> arg : args.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            var rslt = sq.list();
            rslt.forEach(System.out::println);
            return rslt;
        };
        try {
            return tx(command);
        } catch (Exception e) {
            log.error(LOG_MESSAGE, e);
        }
        return new ArrayList<>();
    }

    /**
     * Позволяет выполнять запросы HQL/SQL с параметрами, которые не предполагают извлечение из базы данных объектов
     * @param query шаблон запроса HQL/SQL
     * @param args параметры запроса для формирования запроса к БД
     * @return true при удачном выполнении команды, false при неудачном выполнении команды
     */
    public boolean query(String query, Map<String, Object> args) {
        Function<Session, Boolean> command = session -> {
            var sq = session.createQuery(query);
            args.forEach(sq::setParameter);
            return sq.executeUpdate() > 0;
        };
        try {
            return tx(command);
        } catch (Exception e) {
            log.error(LOG_MESSAGE, e);
        }
        return false;
    }

    /**
     * Запускает транзакцию на выполнение сформированного запроса
     * @param command сессия подключения с базой данных с запросом
     * @return результат выполнения запроса
     * @param <T> тип объекта, возвращаемый в результате выполнения транзакции
     * @throws Exception исключение, возникающее при работе с базой данных
     */
    public <T> T tx(Function<Session, T> command) throws Exception {
        var session = sf.openSession();
        try (session) {
            var tx = session.beginTransaction();
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (Exception e) {
            var tx = session.getTransaction();
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new Exception(e);
        }
    }
}