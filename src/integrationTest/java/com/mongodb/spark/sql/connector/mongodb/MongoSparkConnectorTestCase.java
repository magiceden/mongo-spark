/*
 * Copyright 2008-present MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mongodb.spark.sql.connector.mongodb;

import java.util.HashMap;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.util.CaseInsensitiveStringMap;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.bson.BsonDocument;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.mongodb.spark.sql.connector.config.MongoConfig;

@MongoDBOnline()
public class MongoSparkConnectorTestCase {
  protected static final Logger LOGGER = LoggerFactory.getLogger(MongoSparkConnectorTestCase.class);

  @RegisterExtension
  public static final MongoSparkConnectorHelper MONGODB = new MongoSparkConnectorHelper();

  public String getDatabaseName() {
    return MONGODB.getDatabaseName();
  }

  public String getCollectionName() {
    return MONGODB.getCollectionName();
  }

  public MongoDatabase getDatabase() {
    return MONGODB.getDatabase();
  }

  public MongoCollection<BsonDocument> getCollection() {
    return getCollection(getCollectionName());
  }

  public MongoCollection<BsonDocument> getCollection(final String collectionName) {
    return getDatabase().getCollection(collectionName, BsonDocument.class);
  }

  public CaseInsensitiveStringMap getConnectionProviderOptions() {
    Map<String, String> options = new HashMap<>();
    options.put(
        MongoConfig.PREFIX + MongoConfig.CONNECTION_STRING_CONFIG,
        MONGODB.getConnectionString().toString());
    return new CaseInsensitiveStringMap(options);
  }

  public SparkConf getSparkConf() {
    return MONGODB.getSparkConf();
  }

  public SparkSession getOrCreateSparkSession() {
    return getOrCreateSparkSession(getSparkConf());
  }

  public SparkSession getOrCreateSparkSession(final SparkConf sparkConfig) {
    return SparkSession.builder().sparkContext(getOrCreateSparkContext(sparkConfig)).getOrCreate();
  }

  public SparkContext getOrCreateSparkContext(final SparkConf sparkConfig) {
    return MONGODB.getOrCreateSparkContext(sparkConfig);
  }
}
