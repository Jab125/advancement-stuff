import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Constants {
	private static final Path PROJECT_DIR = findProjectDir();

	private static Path findProjectDir() {
		Path path1 = Path.of(".");
		Path path = path1.toAbsolutePath();
		while (true) {
			if (Files.exists(path.resolve(".root"))) return path;
			path = path.getParent();
			if (path == null) throw new RuntimeException("Failed to find root directory!");
		}
	}

	public static final Path WORKING_DIR = PROJECT_DIR.resolve(".cache");
	public static final Path MC_JAR = Constants.WORKING_DIR.resolve("minecraft.jar");
	public static final Path GENERATED_DIR = PROJECT_DIR.resolve("generated");
	static {
		try {
			Files.createDirectories(WORKING_DIR);
			Files.createDirectories(GENERATED_DIR);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
