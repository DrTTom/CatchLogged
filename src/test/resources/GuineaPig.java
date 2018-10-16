import java.util.logging.Logger;


/**
 * No meaning here.
 *
 * @author TT
 */
public class GuineaPig
{

  private static final Logger LOG = null;

  /**
   * does nothing useful
   *
   * @param args
   */
  public static void main(String[] args)
  {
    try
    {
      // not needed
    }
    catch (RuntimeException e)
    {
      LOG.info("egal {}", "eee".length(), e);
      throw e;
      handleProblem("hallo", e);
    }
  }

  private static void handleProblem(String msg, Exception e)
  {
    // not needed
  }
}
