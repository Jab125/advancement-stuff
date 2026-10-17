import module java.base;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

class MakeRainbowWool {
	private static final String[] COLORS = {"red", "orange", "yellow", "lime", "cyan", "purple", "pink", "gray"};
	static void main() throws IOException, NoSuchAlgorithmException {
		DownloadMinecraftJar.main();
		makeRainbowWool();
		makeRainbowBed();
	}

	static void makeRainbowWool() throws IOException {
		ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(Constants.MC_JAR));
		ZipEntry entry;
		Map<String, String> list = Arrays.stream(COLORS).map(string -> Map.entry(string, "assets/minecraft/textures/block/" + string + "_wool.png"))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (_, _) -> {
							throw new IllegalStateException();
						}, LinkedHashMap::new)
				);
		Map<String, BufferedImage> map = new HashMap<>();
		while ((entry = zipInputStream.getNextEntry()) != null) {
			String name = entry.getName();
			if (list.containsValue(name)) {
				map.put(name, ImageIO.read(new ByteArrayInputStream(zipInputStream.readAllBytes())));
			}
		}
		BufferedImage bufferedImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = bufferedImage.createGraphics();
		int i = 0;
		for (String s : list.values()) {
			graphics.drawImage(map.get(s).getSubimage(0, i, 16, 2), 0, i, null);
			i += 2;
		}
		Path rainbowWool = Constants.GENERATED_DIR.resolve("rainbow_wool");
		Files.createDirectories(rainbowWool);
		ImageIO.write(bufferedImage, "png", Files.newOutputStream(rainbowWool.resolve("rainbow_wool_side.png")));
		ImageIO.write(map.get(list.get(COLORS[0])), "png", Files.newOutputStream(rainbowWool.resolve("rainbow_wool_top.png")));
		ImageIO.write(map.get(list.get(COLORS[COLORS.length-1])), "png", Files.newOutputStream(rainbowWool.resolve("rainbow_wool_bottom.png")));
	}

	// yes we read the jar twice, no, i'm not fixing this
	static void makeRainbowBed() throws IOException {
		ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(Constants.MC_JAR));
		ZipEntry entry;
		Map<String, String> headUpList = Arrays.stream(COLORS).map(string -> Map.entry(string, "assets/minecraft/textures/block/" + string + "_bed_head_up.png"))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (_, _) -> {
							throw new IllegalStateException();
						}, LinkedHashMap::new)
				);
		Map<String, String> footUpList = Arrays.stream(COLORS).map(string -> Map.entry(string, "assets/minecraft/textures/block/" + string + "_bed_foot_up.png"))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (_, _) -> {
							throw new IllegalStateException();
						}, LinkedHashMap::new)
				);
		Map<String, String> headEastList = Arrays.stream(COLORS).map(string -> Map.entry(string, "assets/minecraft/textures/block/" + string + "_bed_head_east.png"))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (_, _) -> {
							throw new IllegalStateException();
						}, LinkedHashMap::new)
				);
		Map<String, String> footEastList = Arrays.stream(COLORS).map(string -> Map.entry(string, "assets/minecraft/textures/block/" + string + "_bed_foot_east.png"))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (_, _) -> {
							throw new IllegalStateException();
						}, LinkedHashMap::new)
				);
		Map<String, String> headWestList = Arrays.stream(COLORS).map(string -> Map.entry(string, "assets/minecraft/textures/block/" + string + "_bed_head_west.png"))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (_, _) -> {
							throw new IllegalStateException();
						}, LinkedHashMap::new)
				);
		Map<String, String> footWestList = Arrays.stream(COLORS).map(string -> Map.entry(string, "assets/minecraft/textures/block/" + string + "_bed_foot_west.png"))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (_, _) -> {
							throw new IllegalStateException();
						}, LinkedHashMap::new)
				);
		Map<String, BufferedImage> headUp = new HashMap<>();
		Map<String, BufferedImage> footUp = new HashMap<>();
		Map<String, BufferedImage> headEast = new HashMap<>();
		Map<String, BufferedImage> footEast = new HashMap<>();
		Map<String, BufferedImage> headWest = new HashMap<>();
		Map<String, BufferedImage> footWest = new HashMap<>();
		BufferedImage bedHeadNorth = null;
		BufferedImage bedFootSouth = null;
		BufferedImage bedDown = null;
		while ((entry = zipInputStream.getNextEntry()) != null) {
			String name = entry.getName();
			if (headUpList.containsValue(name)) {
				headUp.put(name, ImageIO.read(new ByteArrayInputStream(zipInputStream.readAllBytes())));
			} else if (footUpList.containsValue(name)) {
				footUp.put(name, ImageIO.read(new ByteArrayInputStream(zipInputStream.readAllBytes())));
			} else if (headEastList.containsValue(name)) {
				headEast.put(name, ImageIO.read(new ByteArrayInputStream(zipInputStream.readAllBytes())));
			} else if (footEastList.containsValue(name)) {
				footEast.put(name, ImageIO.read(new ByteArrayInputStream(zipInputStream.readAllBytes())));
			} else if (headWestList.containsValue(name)) {
				headWest.put(name, ImageIO.read(new ByteArrayInputStream(zipInputStream.readAllBytes())));
			} else if (footWestList.containsValue(name)) {
				footWest.put(name, ImageIO.read(new ByteArrayInputStream(zipInputStream.readAllBytes())));
			} else if (name.equals("assets/minecraft/textures/block/bed_head_north.png")) {
				bedHeadNorth = ImageIO.read(new ByteArrayInputStream(zipInputStream.readAllBytes()));
			} else if (name.equals("assets/minecraft/textures/block/" + COLORS[COLORS.length-1] + "_bed_foot_south.png")) {
				bedFootSouth = ImageIO.read(new ByteArrayInputStream(zipInputStream.readAllBytes()));
			} else if (name.equals("assets/minecraft/textures/block/bed_down.png")) {
				bedDown = ImageIO.read(new ByteArrayInputStream(zipInputStream.readAllBytes()));
			}
		}
		Map<String, BufferedImage> topFace = new HashMap<>();
		Map<String, BufferedImage> eastFace = new HashMap<>();
		Map<String, BufferedImage> westFace = new HashMap<>();
		for (String color : COLORS) {
			BufferedImage bufferedImage = new BufferedImage(16, 32, BufferedImage.TYPE_INT_ARGB);
			BufferedImage head = headUp.get(headUpList.get(color));
			BufferedImage foot = footUp.get(footUpList.get(color));
			Graphics2D graphics = bufferedImage.createGraphics();
			graphics.drawImage(head, 0, 0, null);
			graphics.drawImage(foot, 0, 16, null);
			topFace.put(color, bufferedImage);

			bufferedImage = new BufferedImage(32, 16, BufferedImage.TYPE_INT_ARGB);
			head = headEast.get(headEastList.get(color));
			foot = footEast.get(footEastList.get(color));
			graphics = bufferedImage.createGraphics();
			graphics.drawImage(head, 16, 0, null);
			graphics.drawImage(foot, 0, 0, null);
			eastFace.put(color, bufferedImage);

			bufferedImage = new BufferedImage(32, 16, BufferedImage.TYPE_INT_ARGB);
			head = headWest.get(headWestList.get(color));
			foot = footWest.get(footWestList.get(color));
			graphics = bufferedImage.createGraphics();
			graphics.drawImage(head, 0, 0, null);
			graphics.drawImage(foot, 16, 0, null);
			westFace.put(color, bufferedImage);
		}
		BufferedImage up = new BufferedImage(16, 32, BufferedImage.TYPE_INT_ARGB);
		Graphics2D upGraphics = up.createGraphics();
		BufferedImage east = new BufferedImage(32, 16, BufferedImage.TYPE_INT_ARGB);
		Graphics2D eastGraphics = east.createGraphics();
		BufferedImage west = new BufferedImage(32, 16, BufferedImage.TYPE_INT_ARGB);
		Graphics2D westGraphics = west.createGraphics();
		int i = 0;
		upGraphics.drawImage(topFace.get(COLORS[0]).getSubimage(0, i, 16, 32), 0, 0, null);
		eastGraphics.drawImage(eastFace.get(COLORS[0]).getSubimage(0, i, 32, 16), 0, 0, null);
		westGraphics.drawImage(westFace.get(COLORS[0]).getSubimage(0, i, 32, 16), 0, 0, null);
		i += 32 - 3 * COLORS.length;
		for (int i1 = 0; i1 < COLORS.length; i1++) {
			upGraphics.drawImage(topFace.get(COLORS[i1]).getSubimage(0, i, 16, 3), 0, i, null);
			eastGraphics.drawImage(eastFace.get(COLORS[i1]).getSubimage(32-i-3, 0, 3, 16), 32-i-3, 0, null);
			westGraphics.drawImage(westFace.get(COLORS[i1]).getSubimage(i, 0, 3, 16), i, 0, null);
			i += 3;
		}
		Path rainbowBed = Constants.GENERATED_DIR.resolve("rainbow_bed");
		Files.createDirectories(rainbowBed);
		ImageIO.write(up.getSubimage(0, 0, 16, 16), "png", Files.newOutputStream(rainbowBed.resolve("rainbow_bed_head_up.png")));
		ImageIO.write(up.getSubimage(0, 16, 16, 16), "png", Files.newOutputStream(rainbowBed.resolve("rainbow_bed_foot_up.png")));
		ImageIO.write(east.getSubimage(16, 0, 16, 16), "png", Files.newOutputStream(rainbowBed.resolve("rainbow_bed_head_east.png")));
		ImageIO.write(east.getSubimage(0, 0, 16, 16), "png", Files.newOutputStream(rainbowBed.resolve("rainbow_bed_foot_east.png")));
		ImageIO.write(west.getSubimage(0, 0, 16, 16), "png", Files.newOutputStream(rainbowBed.resolve("rainbow_bed_head_west.png")));
		ImageIO.write(west.getSubimage(16, 0, 16, 16), "png", Files.newOutputStream(rainbowBed.resolve("rainbow_bed_foot_west.png")));
		ImageIO.write(bedHeadNorth, "png", Files.newOutputStream(rainbowBed.resolve("rainbow_bed_head_north.png")));
		ImageIO.write(bedFootSouth, "png", Files.newOutputStream(rainbowBed.resolve("rainbow_bed_foot_south.png")));
		ImageIO.write(bedDown, "png", Files.newOutputStream(rainbowBed.resolve("rainbow_bed_down.png")));
	}
}