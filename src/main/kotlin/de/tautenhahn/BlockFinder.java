package de.tautenhahn;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BlockFinder
{

  private static final String CATCH = "catch *\\( *\\w+( *\\| *\\w+)* (\\w+) *\\)";

  private static final String BLOCK = "\\{([^\\{\\}]*(\\{[^\\{\\}]*\\}[^\\{\\}]*)*)\\}";

  static Pattern catchBlock = Pattern.compile(CATCH);

  // static final Pattern LOGGED=
  // Pattern.compile("\\w*(?i:log)\\w*.(debug|trace|info|error|warn|fatal)\([^)]")

  public static void main(String[] args)
  {
    String data = " irgendwas } catch  ( DummyException e) { {}{{{}}}    {}}";
    Matcher m = catchBlock.matcher(data);
    System.out.println(m.find());
    System.out.println(m.end());
    System.out.println(captureBlock(data, m.end() + 1, '{', '}'));
  }

  /**
   * Due to recursion, regular expressions are not usable to safely extract block contents.
   */
  private static String captureBlock(String source, int start, char opening, char closing)
  {
    int level = 0;
    int pos = start;
    while (pos < source.length() && source.charAt(pos) != opening)
    {
      pos++;
    }
    level++;
    int endPos = pos + 1;
    while (endPos < source.length() && level > 0)
    {
      if (source.charAt(pos) == opening)
      {
        level++;
      }
      else if (source.charAt(pos) == closing)
      {
        level--;
      }
      endPos++;
    }
    return source.substring(pos + 1, endPos - 1);
  }
}
