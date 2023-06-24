package com.radonlab.tungsten.syntax;

import com.amrdeveloper.codeview.Code;
import com.amrdeveloper.codeview.CodeView;
import com.amrdeveloper.codeview.Keyword;
import com.radonlab.tungsten.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class JavaScriptLanguage {
    // Language Keywords
    private static final String[] KEYWORDS = {};

    private static final Pattern PATTERN_KEYWORDS = Pattern.compile("\\b(abstract|boolean|break|byte|case|catch" +
            "|char|class|continue|default|do|double|else" +
            "|enum|extends|final|finally|float|for|if" +
            "|implements|import|instanceof|int|interface" +
            "|long|native|new|null|package|private|protected" +
            "|public|return|short|static|strictfp|super|switch" +
            "|synchronized|this|throw|transient|try|void|volatile|while)\\b");

    private static final Pattern PATTERN_BUILTINS = Pattern.compile("[,:;[->]{}()]");
    private static final Pattern PATTERN_SINGLE_LINE_COMMENT = Pattern.compile("//[^\\n]*");
    private static final Pattern PATTERN_MULTI_LINE_COMMENT = Pattern.compile("/\\*[^*]*\\*+(?:[^/*][^*]*\\*+)*/");
    private static final Pattern PATTERN_ATTRIBUTE = Pattern.compile("\\.[a-zA-Z0-9_]+");
    private static final Pattern PATTERN_OPERATION = Pattern.compile(":|==|>|<|!=|>=|<=|->|=|>|<|%|-|-=|%=|\\+|\\-|\\-=|\\+=|\\^|\\&|\\|::|\\?|\\*");
    private static final Pattern PATTERN_GENERIC = Pattern.compile("<[a-zA-Z0-9,<>]+>");
    private static final Pattern PATTERN_ANNOTATION = Pattern.compile("@.[a-zA-Z0-9]+");
    private static final Pattern PATTERN_TODO_COMMENT = Pattern.compile("//TODO[^\n]*");
    private static final Pattern PATTERN_NUMBERS = Pattern.compile("\\b(\\d*[.]?\\d+)\\b");
    private static final Pattern PATTERN_CHAR = Pattern.compile("['](.*?)[']");
    private static final Pattern PATTERN_STRING = Pattern.compile("[\"](.*?)[\"]");
    private static final Pattern PATTERN_HEX = Pattern.compile("0x[0-9a-fA-F]+");

    public static void applyTheme(CodeView codeView, ColorTheme colorTheme) {
        codeView.resetSyntaxPatternList();
        codeView.resetHighlighter();
        // View Background
        codeView.setBackgroundColor(colorTheme.getColor(ColorTheme.BLACK));
        // Syntax Colors
        codeView.addSyntaxPattern(PATTERN_HEX, colorTheme.getColor(ColorTheme.ACCENT6));
        codeView.addSyntaxPattern(PATTERN_CHAR, colorTheme.getColor(ColorTheme.ACCENT4));
        codeView.addSyntaxPattern(PATTERN_STRING, colorTheme.getColor(ColorTheme.ACCENT2));
        codeView.addSyntaxPattern(PATTERN_NUMBERS, colorTheme.getColor(ColorTheme.ACCENT6));
        codeView.addSyntaxPattern(PATTERN_KEYWORDS, colorTheme.getColor(ColorTheme.ACCENT1));
        codeView.addSyntaxPattern(PATTERN_BUILTINS, colorTheme.getColor(ColorTheme.WHITE));
        codeView.addSyntaxPattern(PATTERN_SINGLE_LINE_COMMENT, colorTheme.getColor(ColorTheme.DIMMED3));
        codeView.addSyntaxPattern(PATTERN_MULTI_LINE_COMMENT, colorTheme.getColor(ColorTheme.DIMMED3));
        codeView.addSyntaxPattern(PATTERN_ANNOTATION, colorTheme.getColor(ColorTheme.ACCENT1));
        codeView.addSyntaxPattern(PATTERN_ATTRIBUTE, colorTheme.getColor(ColorTheme.ACCENT5));
        codeView.addSyntaxPattern(PATTERN_GENERIC, colorTheme.getColor(ColorTheme.ACCENT1));
        codeView.addSyntaxPattern(PATTERN_OPERATION, colorTheme.getColor(ColorTheme.ACCENT1));
        // Default Color
        codeView.setTextColor(colorTheme.getColor(ColorTheme.WHITE));
        // Tag Color
        codeView.addSyntaxPattern(PATTERN_TODO_COMMENT, colorTheme.getColor(ColorTheme.ACCENT3));

        codeView.reHighlightSyntax();
    }

    public static String[] getKeywords() {
        return KEYWORDS;
    }

    public static List<Code> getCodeList() {
        List<Code> codeList = new ArrayList<>();
        String[] keywords = getKeywords();
        for (String keyword : keywords) {
            codeList.add(new Keyword(keyword));
        }
        return codeList;
    }

    public static Set<Character> getIndentationStarts() {
        Set<Character> characterSet = new HashSet<>();
        characterSet.add('{');
        return characterSet;
    }

    public static Set<Character> getIndentationEnds() {
        Set<Character> characterSet = new HashSet<>();
        characterSet.add('}');
        return characterSet;
    }

    public static String getCommentStart() {
        return "//";
    }

    public static String getCommentEnd() {
        return "";
    }
}