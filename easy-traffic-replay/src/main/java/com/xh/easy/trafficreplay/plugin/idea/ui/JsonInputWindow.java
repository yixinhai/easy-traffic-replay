package com.xh.easy.trafficreplay.plugin.idea.ui;

import com.xh.easy.trafficreplay.plugin.idea.service.MethodDataService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * JSON输入窗口
 * 
 * @author yixinhai
 */
public class JsonInputWindow {

    private JPanel mainPanel;
    private JTextArea jsonTextArea;
    private JLabel methodLabel;
    private JButton submitButton;
    private JButton clearButton;

    private String methodSignature;
    private Object project;

    public JsonInputWindow(Object project, String methodSignature) {
        this.project = project;
        this.methodSignature = methodSignature;
        initComponents();
        setupLayout();
        setupListeners();
    }

    private void initComponents() {
        mainPanel = new JPanel();
        methodLabel = new JLabel("选择的方法: " + methodSignature);
        jsonTextArea = new JTextArea(15, 40);
        submitButton = new JButton("提交JSON");
        clearButton = new JButton("清空");

        // 设置文本区域属性
        jsonTextArea.setLineWrap(true);
        jsonTextArea.setWrapStyleWord(true);
        jsonTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        jsonTextArea.setBorder(BorderFactory.createTitledBorder("JSON输入"));

        // 设置提示文本
        jsonTextArea.setText("请在此输入JSON字符串...\n例如:\n{\n  \"name\": \"test\",\n  \"value\": 123\n}");
    }

    private void setupLayout() {
        mainPanel.setLayout(new BorderLayout());

        // 顶部面板 - 方法签名
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(methodLabel);

        // 中央面板 - JSON输入区域
        JScrollPane scrollPane = new JScrollPane(jsonTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // 底部面板 - 按钮
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(submitButton);
        buttonPanel.add(clearButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupListeners() {
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String jsonContent = jsonTextArea.getText()
                    .trim();
                if (jsonContent.isEmpty() || jsonContent.equals(
                    "请在此输入JSON字符串...\n例如:\n{\n  \"name\": \"test\",\n  \"value\": 123\n}")) {
                    JOptionPane.showMessageDialog(mainPanel, "请输入有效的JSON内容！", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // 保存数据到服务中
                MethodDataService.getInstance()
                    .saveMethodData(methodSignature, jsonContent);

                JOptionPane.showMessageDialog(mainPanel, "数据已保存！\n方法: " + methodSignature + "\nJSON长度: " + jsonContent
                    .length() + " 字符", "成功", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jsonTextArea.setText("请在此输入JSON字符串...\n例如:\n{\n  \"name\": \"test\",\n  \"value\": 123\n}");
            }
        });

        // 点击文本区域时清除提示文本
        jsonTextArea.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (jsonTextArea.getText()
                    .equals("请在此输入JSON字符串...\n例如:\n{\n  \"name\": \"test\",\n  \"value\": 123\n}")) {
                    jsonTextArea.setText("");
                }
            }
        });
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    public String getMethodSignature() {
        return methodSignature;
    }

    public String getJsonContent() {
        return jsonTextArea.getText()
            .trim();
    }
}