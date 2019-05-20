package projectfs;
 
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
public class Fsearch {
	static String help = "Usage: java -jar myjar.jar -dir /dir1 -pattern \"*.{so,md}\"";
	
    public static void main(String[] args) throws Exception {
    	if ( argsParse(args).get("-pattern").size() >= 1) {
    		for (int j = 0; j  < argsParse(args).get("-pattern").size(); j++) {
    		String globPattern = "glob:**/" +  argsParse(args).get("-pattern").get(j); 
                for (int i = 0; i < argsParse(args).get("-dir").size(); i++ )
                    match(globPattern,argsParse(args).get("-dir").get(i));
    	}
    		}
    	else
    		System.out.println(help);
    }
   
    public static Map<String, List<String>> argsParse(String[] args) 
      {
        final Map<String, List<String>> params = new HashMap<>();
       
        
        params.put("-dir", new ArrayList<>());
        params.put("-pattern", new ArrayList<>());
        for (int i = 0; i < args.length; i++) {
            final String a = args[i];
            
            if (a.equals("-dir")  ) {
                i++;
                Path dir = new File(args[i]).toPath();
                if (!Files.isDirectory(dir)) {
                	System.out.println("Invalid Path");
                	return params;
                }
                params.get("-dir").add(args[i]);
            }
            
            
            else if (a.equals("-pattern")) {
            	
                i++;
                if (i < args.length)
                	params.get("-pattern").add(args[i]);
               
            }
        }  
        return params;
    }
   
 
 
    public static void match(String glob, String location) throws IOException {
       
        final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher(
                glob);
       
        Files.walkFileTree(Paths.get(location), new SimpleFileVisitor<Path>() {
           
            @Override
            public FileVisitResult visitFile(Path path,
                    BasicFileAttributes attrs) throws IOException {
                if (pathMatcher.matches(path)) {
                    System.out.println(path);
                }
                return FileVisitResult.CONTINUE;
            }
 
            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc)
                    throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
    }
 
}
