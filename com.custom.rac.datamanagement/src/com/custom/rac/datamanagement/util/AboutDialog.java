package com.custom.rac.datamanagement.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.teamcenter.rac.aif.AbstractAIFDialog;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.util.PropertyLayout;

//交付物清单检查对话框
public class AboutDialog extends AbstractAIFDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static AboutDialog aboutDialog = null;
	private JButton okbtn;

	private AboutDialog() {
		super(AIFUtility.getActiveDesktop(), "关于数据导入");

	}

//单例模式
	public static AboutDialog getInstance() {
		if (null == aboutDialog) {
			aboutDialog = new AboutDialog();
		}
		return aboutDialog;
	}

	public void loadDialog() throws Exception {
		// 初始化
		setSize(600, 350);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		// getRootPane().setBorder(BorderFactory.createLineBorder(Color.GRAY));
		setIconImage(
				Toolkit.getDefaultToolkit().getImage(AboutDialog.class.getResource("/resources/teamcenter_32.png")));
		// 添加一个窗口监听
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				aboutDialog = null;
			}
		});
		JPanel topPanel = new JPanel();
		JPanel mainPanel = new JPanel(new PropertyLayout());
		JPanel btnPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		Font font1 = new Font("宋体", Font.BOLD, 16);
		topPanel.add(new MyLabel("按钮功能介绍", font1));
		// 按钮介绍
		Font font2 = new Font("黑体", Font.PLAIN, 15);
		JLabel lb1 = new MyLabel("加载按钮：", font2);
		JLabel lb2 = new MyLabel("重载按钮：", font2);
		JLabel lb3 = new MyLabel("开始按钮：", font2);
		JLabel lb4 = new MyLabel("暂停按钮：", font2);
		JLabel lb5 = new MyLabel("停止按钮：", font2);
		JLabel lb6 = new MyLabel("大数量导出按钮：", font2);
		JLabel lb7 = new MyLabel("保存按钮：", font2);

		MyTextFiled text1 = new MyTextFiled("选择所要导入PLM系统的Excel表");
		MyTextFiled text2 = new MyTextFiled("重新加载Excel表");
		MyTextFiled text3 = new MyTextFiled("开始读取Excel表中的数据导入PLM系统");
		MyTextFiled text4 = new MyTextFiled("暂停该程序的运行，后边可以单击开始继续上次的导入工作");
		MyTextFiled text5 = new MyTextFiled("停止当前运行的程序");
		MyTextFiled text6 = new MyTextFiled("可选择路径保存当前的数据表");
		MyTextFiled text7 = new MyTextFiled("保存在加载时选择的文件");

		mainPanel.add("1.1.left.center.center", lb1);
		mainPanel.add("1.2.center.center", text1);
		mainPanel.add("2.1.left.center", lb2);
		mainPanel.add("2.2.center.center", text2);
		mainPanel.add("3.1.left.center", lb3);
		mainPanel.add("3.2.center.center", text3);
		mainPanel.add("4.1.left.center", lb4);
		mainPanel.add("4.2.center.center", text4);
		mainPanel.add("5.1.left.center", lb5);
		mainPanel.add("5.2.center.center", text5);
		mainPanel.add("6.1.left.center", lb6);
		mainPanel.add("6.2.center.center", text6);
		mainPanel.add("7.1.left.center", lb7);
		mainPanel.add("7.2.center.center", text7);

		okbtn = new JButton("确定");
		btnPanel.add(okbtn);
		okbtn.addActionListener(this);
		add(topPanel, BorderLayout.NORTH);
		add(mainPanel, BorderLayout.CENTER);
		add(btnPanel, BorderLayout.SOUTH);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == okbtn) {
			aboutDialog = null;
			this.disposeDialog();
		}
	}

	public class MyTextFiled extends JTextField {
		private static final long serialVersionUID = 1L;

		public MyTextFiled(String name) {
			super(name);

			Font font = new Font("黑体", Font.PLAIN, 15);
			this.setFont(font);
			this.setBorder(null);
			this.setEditable(false);
			this.setBackground(Color.WHITE);
		}

	}

	public class MyLabel extends JLabel {
		private static final long serialVersionUID = 1L;

		public MyLabel(String name, Font font) {
			super(name);
			this.setFont(font);
			this.setBackground(Color.WHITE);
		}

	}
}
