/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.beam.fn.harness;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.time.Duration;

/**
 * TransformProcessingThreadTracker tracks the thread ids for the transforms that are being
 * processed in the SDK harness.
 */
public class TransformProcessingThreadTracker {
  private static final TransformProcessingThreadTracker INSTANCE =
      new TransformProcessingThreadTracker();
  private final LoadingCache<Long, String> threadIdToTransformIdMappings;

  private TransformProcessingThreadTracker() {
    this.threadIdToTransformIdMappings =
        CacheBuilder.newBuilder()
            .maximumSize(10000)
            .expireAfterAccess(Duration.ofHours(1))
            .build(
                new CacheLoader<Long, String>() {
                  @Override
                  public String load(Long threadId) throws Exception {
                    return "";
                  }
                });
  }

  public static TransformProcessingThreadTracker getInstance() {
    return INSTANCE;
  }

  public static LoadingCache<Long, String> getThreadIdToTransformIdMappings() {
    return getInstance().threadIdToTransformIdMappings;
  }

  public static void recordProcessingThread(Long threadId, String transformId) {
    getInstance().threadIdToTransformIdMappings.put(threadId, transformId);
  }
}
