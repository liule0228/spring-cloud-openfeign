/*
 * Copyright 2013-2015 the original author or authors.
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

package org.springframework.cloud.netflix.zuul.filters.route.apache;

import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.ribbon.apache.RibbonApacheHttpRequest;
import org.springframework.cloud.netflix.ribbon.apache.RibbonApacheHttpResponse;
import org.springframework.cloud.netflix.ribbon.apache.RibbonLoadBalancingHttpClient;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandContext;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandFactory;
import org.springframework.cloud.netflix.zuul.filters.route.support.AbstractRibbonCommand;

import lombok.RequiredArgsConstructor;

/**
 * @author Christian Lohmann
 */
@RequiredArgsConstructor
public class HttpClientRibbonCommandFactory implements
		RibbonCommandFactory<HttpClientRibbonCommandFactory.HttpClientRibbonCommand> {

	private final SpringClientFactory clientFactory;

	@Override
	public HttpClientRibbonCommand create(final RibbonCommandContext context) {
		final String serviceId = context.getServiceId();
		final RibbonLoadBalancingHttpClient client = this.clientFactory.getClient(
				serviceId, RibbonLoadBalancingHttpClient.class);
		client.setLoadBalancer(this.clientFactory.getLoadBalancer(serviceId));

		return new HttpClientRibbonCommand(serviceId, client, context);
	}

	class HttpClientRibbonCommand extends AbstractRibbonCommand<RibbonLoadBalancingHttpClient, RibbonApacheHttpRequest, RibbonApacheHttpResponse> {

		public HttpClientRibbonCommand(final String commandKey,
									   final RibbonLoadBalancingHttpClient client, RibbonCommandContext context) {
			super(commandKey, client, context);
		}

		@Override
		protected RibbonApacheHttpRequest createRequest() throws Exception {
			return new RibbonApacheHttpRequest(this.context);
		}

	}
}
