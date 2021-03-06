package aohara.tinkertime.views;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import aohara.common.selectorPanel.ControlPanel;
import aohara.tinkertime.models.Mod;

public class ModImageView extends ControlPanel<Mod> {
	
	private final JLabel label = new JLabel();
	
	public ModImageView(){
		panel.add(label);
	}
	
	@Override
	public void display(Mod element){
		if (element != null){
			super.display(element);
			label.setIcon(new ImageIcon(element.getImageUrl()));
		}
	}
}
