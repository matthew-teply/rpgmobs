package com.conanthecivilian.rpgmobs.screen.custom;

import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Button;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Label;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConversationMDParser {
    private static final Pattern WORD_PATTERN = Pattern.compile("\\s*\\S+\\s*");
    private static final Pattern LINK_PATTERN = Pattern.compile("\\[([^\\]]+)\\]\\(([^)]+)\\)");

    public static void parse(
        String rawContent,
        UIElement parentContainer,
        Consumer<String> linkClickCallback
    ) {
        // 1. Split the text into an array of lines based on the newline character
        String[] lines = rawContent.split("\\r?\\n");

        // 2. Loop through every line
        for (int i = 0; i < lines.length; i++) {

            // Parse the links and words for this specific line
            parseLinks(lines[i], parentContainer, linkClickCallback);

            // 3. If this is NOT the last line, insert our hidden line-break element
            if (i < lines.length - 1) {
                UIElement lineBreak = new UIElement();

                // This class MUST exist in your CSS/XML as: width: 100%; height: 0;
                lineBreak.setClasses("line-break");

                parentContainer.addChild(lineBreak);
            }
        }
    }

    private static void parseLinks(
        String lineContent,
        UIElement container,
        Consumer<String> clickCallback
    ) {
        Matcher matcher = LINK_PATTERN.matcher(lineContent);
        int lastEnd = 0;

        while (matcher.find()) {
            String textBefore = lineContent.substring(lastEnd, matcher.start());
            addWords(textBefore, container);

            String buttonText = matcher.group(1);
            String topicIdString = matcher.group(2);

            Button topicButton = new Button();
            topicButton.setText(buttonText);
            topicButton.setClasses("dialogue-inline-button");

            topicButton.setOnClick(event -> {
                clickCallback.accept(topicIdString);
            });

            container.addChild(topicButton);

            lastEnd = matcher.end();
        }

        if (lastEnd < lineContent.length()) {
            String textAfter = lineContent.substring(lastEnd);
            addWords(textAfter, container);
        }
    }

    private static void addWords(String text, UIElement container) {
        if (text == null || text.isEmpty()) return;

        Matcher wordMatcher = WORD_PATTERN.matcher(text);
        while (wordMatcher.find()) {
            Label wordLabel = new Label();
            wordLabel.setText(wordMatcher.group());
            wordLabel.setClasses("font-standard", "width-adaptive", "height-adaptive");
            container.addChild(wordLabel);
        }
    }
}