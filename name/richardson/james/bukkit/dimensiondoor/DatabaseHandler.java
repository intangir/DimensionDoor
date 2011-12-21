
package name.richardson.james.bukkit.dimensiondoor;

import java.util.ArrayList;
import java.util.List;

import com.avaje.ebean.EbeanServer;

public class DatabaseHandler extends name.richardson.james.bukkit.util.Database {

  public DatabaseHandler(final EbeanServer database) {
    super(database);
  }

  public static List<Class<?>> getDatabaseClasses() {
    final List<Class<?>> list = new ArrayList<Class<?>>();
    list.add(WorldRecord.class);
    return list;
  }

  @Override
  public void upgrade(final int schema) {
    // TODO Auto-generated method stub

  }

  @Override
  public void validate() {
    // TODO Auto-generated method stub

  }

}
