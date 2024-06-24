/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jdbi.v3.examples;

import static org.jdbi.v3.examples.order.OrderSupport.withOrders;

import java.util.List;
import java.util.Map;

import org.jdbi.v3.sqlobject.config.*;
import org.jdbi.v3.sqlobject.statement.*;

/**
 * Checks if DAO interface inheritance is working.
 */
@SuppressWarnings({ "PMD.SystemPrintln" })
public final class DaoInheritance {

    private DaoInheritance() {
        throw new AssertionError("DaoInheritance can not be instantiated");
    }

    public static void main(String... args) throws Exception {
        withOrders(jdbi -> {

            System.out.printf("OrderDaoBase%n");
            OrderDaoBase orderDaoBase = jdbi.onDemand(OrderDaoBase.class);

            List<Integer> top3Ids = orderDaoBase.getTop3Ids();
            top3Ids.forEach(id -> System.out.printf("top3Ids: %d%n", id));
        });

        withOrders(jdbi -> {

            System.out.printf("OrderDao%n");
            OrderDao orderDao = jdbi.onDemand(OrderDao.class);

            List<Integer> top3Ids = orderDao.getTop3Ids();
            top3Ids.forEach(id -> System.out.printf("top3Ids: %d%n", id));

            Map<Integer, Integer> orderIdUserIdMap = orderDao.getOrderIdUserIdMap();
            orderIdUserIdMap.forEach((k, v) -> System.out.printf("idUserId: %d -> %d%n", k, v));
        });

        withOrders(jdbi -> {

            System.out.printf("OrderDaoMultiExtend%n");
            OrderDaoMultiExtend orderDaoMultiExtend = jdbi.onDemand(OrderDaoMultiExtend.class);

            List<Integer> top3Ids = orderDaoMultiExtend.getTop3Ids();
            top3Ids.forEach(id -> System.out.printf("top3Ids: %d%n", id));

            Map<Integer, Integer> orderIdUserIdMap = orderDaoMultiExtend.getOrderIdUserIdMap();
            orderIdUserIdMap.forEach((k, v) -> System.out.printf("idUserId: %d -> %d%n", k, v));

            List<Integer> top3userIds = orderDaoMultiExtend.getTop3UserIds();
            top3userIds.forEach(id -> System.out.printf("top3userIds: %d%n", id));
        });
    }

    public interface OrderDaoBase {

        @SqlQuery("select id from orders limit 3")
        List<Integer> getTop3Ids();

    }

    public interface OrderDao extends OrderDaoBase {

        @SqlQuery("select id, user_id from orders limit 3")
        @KeyColumn("id")
        @ValueColumn("user_id")
        Map<Integer, Integer> getOrderIdUserIdMap();

    }

    public interface OrderDaoMultiExtend extends OrderDao, OrderDaoBase {

        @SqlQuery("select distinct user_id from orders limit 3")
        List<Integer> getTop3UserIds();
    }

}