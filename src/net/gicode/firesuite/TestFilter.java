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
 * Implementations of this interface serve as a specification of test cases to
 * include in a firesuite.
 * 
 * @author rusty
 */
public interface TestFilter {

  /**
   * Used to determine if the given Class should be included in the firesuite.
   * Only classes castable to TestCase will be provided for questioning.
   * 
   * @param testClass The Class to make a decision about.
   * @return true if the provided class should be included in the suite.
   */
  public boolean includeClass(Class<?> testClass);

  /**
   * Used to determine if the given Method should be included in the firesuite.
   * All methods defined by included classes will be provided for questioning.
   * It is the implementors responsibility to determine if this method is even a
   * valid test method.
   * 
   * @param testMethod The Method to make a decision about.
   * @return true if the provided method should be included in the suite.
   */
  public boolean includeMethod(Method testMethod);

  /**
   * This method must return a package name with at least one part, null and ""
   * are not valid. It is the implementors responsibility to make certain the
   * package name is valid and exists in the runtime classpath.
   * 
   * @return A valid package name.
   */
  public String getPackageRoot();
}
