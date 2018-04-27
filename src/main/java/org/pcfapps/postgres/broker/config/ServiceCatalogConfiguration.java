/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.pcfapps.postgres.broker.config;

import org.springframework.cloud.servicebroker.model.catalog.Catalog;
import org.springframework.cloud.servicebroker.model.catalog.Plan;
import org.springframework.cloud.servicebroker.model.catalog.ServiceDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceCatalogConfiguration {
	@Bean
	public Catalog catalog() {
		Plan plan = Plan.builder()
				.id("d18eb918-b9c1-458c-af2c-5435dfa41c80")
				.name("standard")
				.description("simple plan")
				.free(true)
				.build();

		ServiceDefinition serviceDefinition = ServiceDefinition.builder()
				.id("f2aba675-fbe3-4400-adb5-25aaef9505b9")
				.name("postgres")
				.description("A simple postgres service")
				.bindable(true)
				.tags("postgres")
				.plans(plan)
				.metadata("displayName", "PostgresSQL")
				.metadata("imageUrl","/images/postgres.png")
				.metadata("longDescription", "A simple postgres service")
				.metadata("providerDisplayName", "PostgreSQL")
				.metadata("documentationUrl","https://github.com/jigsheth57/postgres-service-broker")
				.build();

		return Catalog.builder()
				.serviceDefinitions(serviceDefinition)
				.build();
	}
}
