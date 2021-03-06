package org.ff4j.test.cache;

/*
 * #%L
 * ff4j-core
 * %%
 * Copyright (C) 2013 Ff4J
 * %%
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
 * #L%
 */

import org.junit.Assert;

import org.ff4j.cache.FeatureStoreCacheProxy;
import org.ff4j.cache.InMemoryCacheManager;
import org.ff4j.core.Feature;
import org.ff4j.core.FeatureStore;
import org.ff4j.store.InMemoryFeatureStore;
import org.ff4j.test.store.AbstractStoreTest;
import org.junit.Test;

/**
 * Testing class of {@link InMemoryCacheManager} class.
 * 
 * @author <a href="mailto:cedrick.lunven@gmail.com">Cedrick LUNVEN</a>
 */
public class InMemoryCacheTest extends AbstractStoreTest {

    /** {@inheritDoc} */
    @Override
    public FeatureStore initStore() {
        return new FeatureStoreCacheProxy(new InMemoryFeatureStore("ff4j.xml"), new InMemoryCacheManager());
    }

    @Test
    public void testInitializations() {
        InMemoryCacheManager fcm = new InMemoryCacheManager();
        fcm.setTtl(10);
        Assert.assertEquals(10, fcm.getTtl());
        Assert.assertNotNull(fcm.getNativeCache());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullisIlegal() {
        new InMemoryCacheManager().put(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullFeatureId() {
        Feature f = new Feature("a");
        f.setUid(null);
        new InMemoryCacheManager().put(f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutEmptyFeatureId() {
        Feature f = new Feature("a");
        f.setUid("");
        new InMemoryCacheManager().put(f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequiredArgumentTarget() {
        new FeatureStoreCacheProxy().getTarget();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequiredArgumentCacheManager() {
        new FeatureStoreCacheProxy().getCacheManager();
    }

    @Test
    public void testExistBis() {
        FeatureStoreCacheProxy fscp = new FeatureStoreCacheProxy();
        fscp.setCacheManager(new InMemoryCacheManager());
        fscp.setTarget(new InMemoryFeatureStore("ff4j.xml"));
        Assert.assertFalse(fscp.exist("toto"));
        Assert.assertFalse(fscp.exist("toto"));
        Assert.assertTrue(fscp.exist("first"));
        Assert.assertTrue(fscp.exist("first"));
    }

}
