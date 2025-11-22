/*
 * Copyright 2024-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.cloud.ai.autoconfigure.mcp.discovery.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.cloud.ai.mcp.discovery.client.transport.DistributedAsyncMcpClient;
import com.alibaba.cloud.ai.mcp.discovery.client.transport.DistributedSyncMcpClient;
import com.alibaba.cloud.ai.mcp.discovery.client.transport.streamable.StreamWebFluxDistributedAsyncMcpClient;
import com.alibaba.cloud.ai.mcp.discovery.client.transport.streamable.StreamWebFluxDistributedSyncMcpClient;
import com.alibaba.cloud.ai.mcp.nacos.NacosMcpClientProperties;
import com.alibaba.cloud.ai.mcp.nacos.NacosMcpStreamableClientProperties;
import com.alibaba.cloud.ai.mcp.nacos.service.NacosMcpOperationService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author yingzi
 * @since 2025/10/28
 */
@AutoConfiguration
@EnableConfigurationProperties({ NacosMcpStreamableClientProperties.class, NacosMcpClientProperties.class})
@ConditionalOnProperty(prefix = "spring.ai.alibaba.mcp.nacos.client", name = { "enabled" }, havingValue = "true",
        matchIfMissing = false)
public class NacosMcpStreamableClientAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "spring.ai.mcp.client", name = { "type" }, havingValue = "SYNC",
            matchIfMissing = true)
    public List<DistributedSyncMcpClient> streamableWebFluxDistributedSyncClients(
            Map<String, NacosMcpOperationService> nacosMcpOperationServiceMap,
            NacosMcpStreamableClientProperties nacosMcpStreamableClientProperties, ApplicationContext applicationContext) {
        List<DistributedSyncMcpClient> clients = new ArrayList<>();
        nacosMcpStreamableClientProperties.getConnections().forEach((name, nacosSseParameters) -> {
            StreamWebFluxDistributedSyncMcpClient client = StreamWebFluxDistributedSyncMcpClient.builder()
                    .serverName(nacosSseParameters.serviceName())
                    .version(nacosSseParameters.version())
                    .nacosMcpOperationService(nacosMcpOperationServiceMap.get(name))
                    .applicationContext(applicationContext)
                    .build();
            client.init();
            client.subscribe();
            clients.add(client);
        });
        return clients;
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.ai.mcp.client", name = { "type" }, havingValue = "ASYNC",
            matchIfMissing = true)
    public List<DistributedAsyncMcpClient> streamableWebFluxDistributedAsyncClients(
            Map<String, NacosMcpOperationService> nacosMcpOperationServiceMap,
            NacosMcpStreamableClientProperties nacosMcpStreamableClientProperties, ApplicationContext applicationContext) {
        List<DistributedAsyncMcpClient> clients = new ArrayList<>();
        nacosMcpStreamableClientProperties.getConnections().forEach((name, nacosSseParameters) -> {
            StreamWebFluxDistributedAsyncMcpClient client = StreamWebFluxDistributedAsyncMcpClient.builder()
                    .serverName(nacosSseParameters.serviceName())
                    .version(nacosSseParameters.version())
                    .nacosMcpOperationService(nacosMcpOperationServiceMap.get(name))
                    .applicationContext(applicationContext)
                    .build();
            client.init();
            client.subscribe();
            clients.add(client);
        });
        return clients;
    }
}
