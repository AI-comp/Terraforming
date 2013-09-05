using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AppletPageGenerator
{
    class Program
    {
        static void Main(string[] args) {
            var html = @"
<html>
<body>
<applet archive='Terraforming.jar' code='net/aicomp/terraforming/AppletReplayMain.class' width='100' height='100'>
	<param name='fps' value='60'>
	<param name='replay' value='replay/XXX'>
</applet>
</body>
</html>
";
            var dir = new DirectoryInfo(@"C:\Users\exKAZUu\Projects\ai-comp.github.com\cedec2013\applet\final_replay");
            foreach (var repFile in dir.EnumerateFiles("*.rep")) {
                var newHtml = html.Replace("XXX", repFile.Name);
                File.WriteAllText(@"C:\Users\exKAZUu\Projects\ai-comp.github.com\cedec2013\applet\" + Path.ChangeExtension(repFile.Name, "html"), newHtml);
            }
        }
    }
}
