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
import java.util.regex.Pattern;

/**
 * Defines a filter to include based on the provided regex for class and method.
 * 
 * @author rusty
 */
public class RegexTestFilter extends DefaultTestFilter {

  private final Pattern classPattern;
  private final Pattern methodPattern;

  /**
   * @param packageRoot The package name to search under.
   * @param classRegex A regex that matches the test classes to execute.
   */
  public RegexTestFilter(String packageRoot, String classRegex) {
    this(packageRoot, classRegex, null);
  }

  /**
   * @param packageRoot The package name to search under.
   * @param classRegex A regex that matches the test classes to execute.
   * @param methodRegex A regex that matches the test methods to execute.
   */
  public RegexTestFilter(String packageRoot, String classRegex,
                         String methodRegex) {
    super(packageRoot);

    this.classPattern = Pattern.compile(classRegex);
    this.methodPattern = (methodRegex != null) ? Pattern.compile(methodRegex)
        : Pattern.compile("");
  }

  @Override
  public boolean includeClass(Class<?> testClass) {
    return this.classPattern.matcher(testClass.getCanonicalName()).find();
  }

  @Override
  public boolean includeMethod(Method testMethod) {
    return this.methodPattern.matcher(testMethod.getName()).find()
        && super.includeMethod(testMethod);
  }
}
