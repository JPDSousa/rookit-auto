/*******************************************************************************
 * Copyright (C) 2018 Joao Sousa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package org.rookit.auto.config;

import org.rookit.config.Configuration;
import org.rookit.io.path.PathConfig;
import org.rookit.io.path.PathConfigFactory;

final class AutoConfigImpl implements AutoConfig {

    private final Configuration configuration;
    private final PathConfigFactory pathConfigFactory;

    AutoConfigImpl(final Configuration configuration,
                   final PathConfigFactory pathConfigFactory) {
        this.configuration = configuration;
        this.pathConfigFactory = pathConfigFactory;
    }

    @Override
    public PathConfig fileConfig() {
        return this.pathConfigFactory.create(this.configuration.getConfig("path"));
    }

    // TODO we may be able to generalize this logic
    @Override
    public Configuration getProcessorConfig(final String name) {
        return this.configuration.getConfig(name);
    }

    @Override
    public String toString() {
        return "AutoConfigImpl{" +
                "configuration=" + this.configuration +
                ", pathConfigFactory=" + this.pathConfigFactory +
                "}";
    }
}
