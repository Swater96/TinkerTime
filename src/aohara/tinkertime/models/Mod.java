package aohara.tinkertime.models;

import java.net.URL;
import java.util.Date;

import aohara.tinkertime.controllers.ModManager.CannotAddModException;

public class Mod extends ModApi{
	
	private String name, creator, currentFile;
	private Date lastUpdated;
	private URL downloadLink, imageUrl, pageUrl;
	private boolean enabled = false;
	private transient boolean updateAvailable = false;
	
	public Mod(ModApi page) throws CannotAddModException {
		updateModData(page);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Date getUpdatedOn() {
		return lastUpdated;
	}

	@Override
	public String getCreator() {
		return creator;
	}

	@Override
	public String getNewestFile() {
		return currentFile;
	}

	@Override
	public URL getDownloadLink() {
		return downloadLink;
	}

	@Override
	public URL getImageUrl() {
		return imageUrl;
	}

	@Override
	public URL getPageUrl() {
		return pageUrl;
	}
	
	// -- Other Methods --------------------
	
	public boolean isEnabled(){
		return enabled;
	}
	
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
	
	public void updateModData(ModApi mod) throws CannotAddModException{
		try{
			name = mod.getName();
			creator = mod.getCreator();
			currentFile = mod.getNewestFile();
			
			lastUpdated = mod.getUpdatedOn();
			
			downloadLink = mod.getDownloadLink();
			imageUrl = mod.getImageUrl();
			pageUrl = mod.getPageUrl();
			
			updateAvailable = false;
		} catch(NullPointerException e){
			e.printStackTrace();
			throw new CannotAddModException();
		}
	}
	
	public void setUpdateAvailable(){
		updateAvailable = true;
	}
	
	public boolean isUpdateAvailable(){
		return updateAvailable;
	}
	
	@Override
	public boolean equals(Object o){
		if (o instanceof Mod){
			return ((Mod)o).getName().equals(getName());
		}
		return false;
	}
}
