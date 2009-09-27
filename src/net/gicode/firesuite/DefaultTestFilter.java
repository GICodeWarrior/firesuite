/**
 * Copyright 2009 Rusty Burchfield
 *
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
 *
 * This file is part of Firesuite.
 */
package net.gicode.firesuite;

import java.lang.reflect.Method;

/**
 * This is a simple firesuite filter that will include all tests with names
 * starting with "test" or with the @Test annotation and in the given package.
 * 
 * @author rusty
 */
public class DefaultTestFilter implements TestFilter {

  private final String packageRoot;

  public DefaultTestFilter(String packageRoot) {
    this.packageRoot = packageRoot;
  }

  @Override
  public boolean includeClass(Class<?> testClass) {
    return true;
  }

  @Override
  public boolean includeMethod(Method testMethod) {
    return testMethod.getName().startsWith("test")
        || testMethod.getAnnotation(org.junit.Test.class) != null;
  }

  @Override
  public String getPackageRoot() {
    return this.packageRoot;
  }
}
