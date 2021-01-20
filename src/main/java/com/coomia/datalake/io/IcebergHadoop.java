/*
 * Copyright (c) UMX Technology Co., Ltd. All Rights Reserved. This software is licensed not sold.
 * Use or reproduction of this software by any unauthorized individual or entity is strictly
 * prohibited. This software is the confidential and proprietary information of UMX Technology Co.,
 * Ltd. Disclosure of such confidential information and shall use it only in accordance with the
 * terms of the license agreement you entered into with UMX Technology Co., Ltd.
 * 
 * UMX Technology Co., Ltd. MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. UMX Technology Co., Ltd.
 * SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ANY DERIVATIVES THEREOF.
 */
// Created on 2021年1月14日

package com.coomia.flink.demo;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;

/**
 * @author spancer
 *
 */
public class IcebergHadoop {

  /**
   * @param args
   */
  public static void main(String[] args) {

    System.setProperty("HADOOP_USER_NAME", "hdfs");

    EnvironmentSettings settings = EnvironmentSettings.newInstance().useBlinkPlanner() // 使用BlinkPlanner
        .build();
    TableEnvironment tenv = TableEnvironment.create(settings);
    String createIcebergCatalog =
        "CREATE CATALOG iceberg WITH ( 'type'='iceberg', 'catalog-type'='hadoop', 'clients'='5', 'property-version'='1', 'warehouse'='hdfs://itserver21:8020/warehouse')";
    tenv.executeSql(createIcebergCatalog);

    boolean exists = true;
    tenv.executeSql("show catalogs").print();
    tenv.useCatalog("iceberg");
    if (!exists)
      tenv.executeSql("CREATE DATABASE icebergdb");
    tenv.useDatabase("icebergdb");
    if (!exists)
      tenv.executeSql("CREATE TABLE iceberg.icebergdb.sample (\r\n"
          + "id BIGINT COMMENT 'unique id',\r\n" + "data STRING\r\n" + ")");
    tenv.executeSql("show tables").print();

    tenv.executeSql("INSERT INTO iceberg.icebergdb.sample VALUES (2, 'b')");
    tenv.executeSql("select * from iceberg.icebergdb.sample limit 10 ").print();

  }

}
