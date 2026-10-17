import module java.base;

class DownloadMinecraftJar {
	static final String URL = "https://piston-data.mojang.com/v1/objects/e877b6a07acd633fb3bb475002175cec036e7b87/client.jar";
	static final String HASH = "e877b6a07acd633fb3bb475002175cec036e7b87";

	static void main() throws IOException, NoSuchAlgorithmException {
		if (!fileAlreadyExists()) {
			byte[] bytes;
			try (InputStream stream = URI.create(URL).toURL().openStream()){
				bytes = stream.readAllBytes();
			}
			Files.write(Constants.MC_JAR, bytes);
		}
	}

	static boolean fileAlreadyExists() throws NoSuchAlgorithmException, IOException {
		MessageDigest instance = MessageDigest.getInstance("SHA-1");
		if (Files.exists(Constants.MC_JAR) && Arrays.equals(HexFormat.of().parseHex(HASH), instance.digest(Files.readAllBytes(Constants.MC_JAR)))) return true;
		return false;
	}
}