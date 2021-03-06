/**
 * Renjin : JVM-based interpreter for the R language for the statistical analysis
 * Copyright © 2010-2016 BeDataDriven Groep B.V. and contributors
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, a copy is available at
 * https://www.gnu.org/licenses/gpl-2.0.txt
 */
package org.renjin.gcc;

import org.renjin.gcc.gimple.GimpleFunction;
import org.renjin.gcc.symbols.SymbolTable;
import org.renjin.repackaged.asm.tree.MethodNode;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Supports detailed logging of the compilation process
 */
public abstract class TreeLogger {


  public abstract boolean isEnabled();

  public enum Level {
    INFO,
    DEBUG
  }

  public abstract PrintWriter debugLog(String name);

  public final TreeLogger branch(String message) {
    return branch(Level.INFO, message);
  }
  
  public final void debug(String message) {
    log(Level.DEBUG, message);
  }

  public abstract void dump(String dir, String file, String ext, Object value);

  public abstract void dumpHtml(SymbolTable symbolTable, GimpleFunction gimpleFunction, MethodNode methodNode);

  public abstract void log(Level level, String message);

  public abstract TreeLogger branch(Level level, String message);
  
  public abstract TreeLogger debug(String message, Object code);

  public abstract void finish() throws IOException;

  
}
