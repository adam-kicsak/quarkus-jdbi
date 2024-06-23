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

import java.util.Map;

import org.jdbi.v3.sqlobject.config.*;
import org.jdbi.v3.sqlobject.statement.*;

/**
 * Checks if {@link KeyColumn} and {@link ValueColumn} annotations are working.
 */
@SuppressWarnings({ "PMD.SystemPrintln" })
public final class KeyColumnAndValueColumn {

    private KeyColumnAndValueColumn() {
        throw new AssertionError("KeyColumnAndValueColumn can not be instantiated");
    }

    public static void main(String... args) throws Exception {
        withOrders(jdbi -> {

            OrderDao orderDao = jdbi.onDemand(OrderDao.class);
            Map<Integer, Integer> orderIdUserIdMap = orderDao.getOrderIdUserIdMap();

            orderIdUserIdMap.forEach((k, v) -> System.out.printf("%d -> %d%n", k, v));

        });
    }

    public interface OrderDao {

        @SqlQuery("select id, user_id from orders limit 3")
        @KeyColumn("id")
        @ValueColumn("user_id")
        Map<Integer, Integer> getOrderIdUserIdMap();

    }

}