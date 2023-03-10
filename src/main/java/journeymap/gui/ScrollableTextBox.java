package journeymap.gui;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class ScrollableTextBox extends ScrollableField
{
	public int textFieldX;
	public int textFieldY;
	public int textFieldWidth;
	private static int textFieldHeight = 12;

	public List<String> scrollableElements;

	protected GuiTextField textField;

	ScrollableTextBox(int x, int y, int width, String label, FontRenderer fontrendererObj)
	{
		super(x, y, width, label, fontrendererObj);
		this.init();
	}

	ScrollableTextBox(int x, int y, int width, String label, List<String> scrollableElements, FontRenderer fontrendererObj)
	{
		super(x, y, width, label, fontrendererObj);
		this.scrollableElements = scrollableElements;
		this.init();
	}

	private void init()
	{
		this.textFieldX = this.x + ScrollableField.arrowsWidth + 3;
		this.textFieldY = this.y;
		this.textFieldWidth = this.width - 5 - (ScrollableField.arrowsWidth * 2);

		this.textField = new GuiTextField(0, this.fontrendererObj, this.textFieldX, this.textFieldY, this.textFieldWidth, ScrollableTextBox.textFieldHeight);

		this.textField.setMaxStringLength(32);

	}

	@Override
	public void draw()
	{
		super.draw();
		this.textField.drawTextBox();
		if (!this.validateTextFieldData())
		{
			// draw a red rectangle over the textbox to indicate that the text
			// is invallid
			int x1 = this.textFieldX - 1;
			int y1 = this.textFieldY - 1;
			int x2 = this.textFieldX + this.textFieldWidth;
			int y2 = this.textFieldY + ScrollableTextBox.textFieldHeight;
			int colour = 0xff900000;

			this.drawHorizontalLine(x1, x2, y1, colour);
			this.drawHorizontalLine(x1, x2, y2, colour);

			this.drawVerticalLine(x1, y1, y2, colour);
			this.drawVerticalLine(x2, y1, y2, colour);
		}
	}

	@Override
	public void mouseClicked(int x, int y, int button)
	{
		super.mouseClicked(x, y, button);
		this.textField.mouseClicked(x, y, button);
	}

	public void mouseDWheelScrolled(int x, int y, int direction)
	{
		if (this.posWithinTextField(x, y))
		{
			this.textFieldScroll(-direction);
		}
	}

	public boolean validateTextFieldData()
	{
		return this.getText().length() > 0;
	}

	public boolean posWithinTextField(int x, int y)
	{
		return (x >= this.textFieldX) && (y >= this.textFieldY) && (x <= (this.textFieldWidth + this.textFieldX)) && (y <= (ScrollableTextBox.textFieldHeight + this.textFieldY));
	}

	public void textFieldScroll(int direction)
	{
		if (this.scrollableElements != null)
		{
			int index = this.scrollableElements.indexOf(this.getText().trim());
			if (direction > 0)
			{
				if ((index == -1) || (index == (this.scrollableElements.size() - 1)))
				{
					index = 0;
				}
				else
				{
					index++;
				}
			}
			else if (direction < 0)
			{
				if ((index == -1) || (index == 0))
				{
					index = this.scrollableElements.size() - 1;
				}
				else
				{
					index--;
				}
			}
			this.textField.setText(this.scrollableElements.get(index));
		}
	}

	@Override
	public void nextElement()
	{
		this.textFieldScroll(1);
	}

	@Override
	public void previousElement()
	{
		this.textFieldScroll(-1);
	}

	public String getText()
	{
		return this.textField.getText();
	}

	public void setText(String text)
	{
		this.textField.setText(text);
	}

	@Override
	public void setFocused(Boolean focus)
	{
		this.textField.setFocused(focus);
		this.textField.setSelectionPos(0);
	}

	@Override
	public Boolean isFocused()
	{
		return this.textField.isFocused();
	}

	public void KeyTyped(char c, int key)
	{
		this.textField.textboxKeyTyped(c, key);
	}

	public int getCursorPosition()
	{
		return this.textField.getCursorPosition();
	}

	public void setCursorPositionEnd()
	{
		this.textField.setCursorPositionEnd();
	}
}
