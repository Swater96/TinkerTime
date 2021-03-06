package aohara.tinkertime.models;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import aohara.tinkertime.controllers.ModManager.CannotAddModException;

public class ModPage extends ModApi {
	
	private static Pattern ID_PATTERN = Pattern.compile("(\\d{4})(\\d{3})");
	
	private final Element doc;
	private final URL pageUrl;
	
	public static ModPage createFromFile(Path path, URL pageUrl) 
			throws CannotAddModException{
		try {
			Element ele = Jsoup.parse(path.toFile(), "UTF-8");
			return new ModPage(ele, pageUrl);
		} catch (IOException e) {
			throw new CannotAddModException();
		}
	}
	
	public ModPage(Element doc, URL pageUrl){
		this.doc = doc;
		this.pageUrl = pageUrl;
	}

	
	@Override
	public String getName(){
		Element ele = doc.getElementById("project-overview");
		ele = ele.getElementsByClass("caption").first();
		return ele.text();
	}
	
	@Override
	public Date getUpdatedOn(){
		Element ele = doc.getElementById("project-overview");
		ele = ele.getElementsContainingOwnText("Updated").first();
		String dateText = ele.text().replace("Updated", "").trim();
		
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(dateText);
		} catch (ParseException e) {
			return null;
		}
	}
	
	@Override
	public String getCreator(){
		Element ele = doc.getElementById("project-overview");
		ele = ele.getElementsContainingOwnText("Manager").first();
		return ele.text().split(":")[1].trim();
	}
	
	@Override
	public String getNewestFile(){
		Element ele = doc.getElementById("project-overview");
		ele = ele.getElementsContainingOwnText("Newest File").first();
		return ele.text().split(":")[1].trim();
	}
	
	@Override
	public URL getDownloadLink() {
		Element ele = doc.select("#tab-other-downloads tr.even a").first();
		Matcher m = ID_PATTERN.matcher(ele.attr("href"));
		
		// Get Mod ids
		m.find();
		int id1 = Integer.parseInt(m.group(1));
		int id2 = Integer.parseInt(m.group(2));
		
		URL url = null;
		try {
			String urlString = String.format(
				"http://addons.curse.cursecdn.com/files/%s/%s/%s",
				id1, id2,
				getNewestFile().replaceAll(" ", "%20")
			);
			
			url = testUrl(urlString);
			if (url == null){
				url = testUrl(urlString.replaceAll("_", "%20"));
			}			
			
		} catch (IOException | IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		return url;
	}
	
	private URL testUrl(String urlString) throws IOException{
		URL url = new URL(urlString);
		if (url.openConnection().getContentLength() > 0){
			return url;
		}
		return null;
	}
	
	@Override
	public URL getImageUrl(){
		try {
			Element ele = doc.select("img.primary-project-attachment").first();
			return new URL(ele.attr("src"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public URL getPageUrl(){
		return pageUrl;
	}
	
	@SuppressWarnings("serial")
	public class CannotScrapeException extends Throwable {}
}
