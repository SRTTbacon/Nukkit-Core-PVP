package srttbacon.plugin.Class;

import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.element.ElementSlider;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowModal;

public class Form_Window
{
    public static class CustomForm
    {
        FormWindowCustom form = null;
        public CustomForm(String Title)
        {
            form = new FormWindowCustom(Title);
        }
        public void Add_TextBlock(String Text)
        {
            form.addElement(new ElementLabel(Text));
        }
        public void Add_ComboBox(String Text, List<String> Contents, int DefaultIndex)
        {
            form.addElement(new ElementDropdown(Text, Contents, DefaultIndex));
        }
        public void Add_Slider(String Text, float Min, float Max, float Default)
        {
            form.addElement(new ElementSlider(Text, Min, Max, 1, Default));
        }
        public void Show(Player p)
        {
            p.showFormWindow(form);
        }
    }
    public static class ModalForm
    {
        FormWindowModal form = null;
        public ModalForm(String Title, String Button_Text_01, String Button_Text_02)
        {
            form = new FormWindowModal(Title, "", Button_Text_01, Button_Text_02);
        }
        public void Set_Content_Text(String Text)
        {
            form.setContent(Text);
        }
        public void Show(Player p)
        {
            p.showFormWindow(form);
        }
    }
}