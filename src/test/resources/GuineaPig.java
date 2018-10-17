import java.util.logging.Logger;

import org.slf4j.LoggerFactory;


/**
 * No meaning here.
 *
 * @author TT
 */
public class GuineaPig
{

  // Wrong category on purpose:
  private static final Logger LOG = LoggerFactory.getLogger(String.class);

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
      // Excessive use of information, on purpose:
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
