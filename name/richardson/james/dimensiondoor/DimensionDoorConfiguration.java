/*******************************************************************************
 * Copyright (c) 2011 James Richardson.
 * 
 * DimensionDoorConfiguration.java is part of DimensionDoor.
 * 
 * DimensionDoor is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * DimensionDoor is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * DimensionDoor. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package name.richardson.james.dimensiondoor;

import java.io.IOException;
import java.io.InputStream;

import name.richardson.james.dimensiondoor.util.Configuration;
import name.richardson.james.dimensiondoor.util.Logger;

public class DimensionDoorConfiguration extends Configuration {

  protected final static String fileName = "config.yml";
  protected final static Logger logger = new Logger(DimensionDoorConfiguration.class);

  protected final InputStream defaults = DimensionDoor.getInstance().getResource(fileName);

  public DimensionDoorConfiguration() throws IOException {
    super();
  }

  public static DimensionDoorConfiguration getInstance() {
    return (DimensionDoorConfiguration) instance;
  }

  public boolean isClearActionBar() {
    return configuration.getBoolean("creative-world-transfer-settings.clear-action-bar");
  }

  public boolean isClearHand() {
    return configuration.getBoolean("creative-world-transfer-settings.clear-hand");
  }

  public boolean isDebugging() {
    return configuration.getBoolean("debugging");
  }

  public boolean isPreventContainerBlocks() {
    return configuration.getBoolean("creative-world-settings.prevent-container-blocks");
  }

  public boolean isPreventItemSpawning() {
    return configuration.getBoolean("creative-world-settings.prevent-item-spawning");
  }

  public void logValues() {
    logger.config(String.format("debugging : %b", this.isDebugging()));
    logger.config(String.format("inventory-settings.clear-action-bar : %b", this.isClearActionBar()));
    logger.config(String.format("inventory-settings.clear-hand : %b", this.isClearHand()));
    logger.config(String.format("world-settings.prevent-container-blocks : %b", this.isPreventContainerBlocks()));
  }

}
