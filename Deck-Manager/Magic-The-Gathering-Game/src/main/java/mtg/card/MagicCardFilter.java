package mtg.card;

import com.reflexit.magiccards.core.model.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import mtg.card.MagicCardFilter.SearchToken.TokenType;

public class MagicCardFilter implements ICardFilter {
    public static CardFilterExpr TRUE = new CardFilterExpr() {
        @Override
        public boolean evaluate(Object o) {
            return true;
        }

        @Override
        public Object getFieldValue(ICardField o) {
            return Boolean.TRUE;
        }

        @Override
        public String toString() {
            return "true";
        }
    };

    private CardFilterExpr root;
    private static final Logger LOG = Logger.getLogger(MagicCardFilter.class.getName());

    static MagicCardFilter.BinaryExpr ignoreCase1SearchDb(ICardField field, String value) {
        char c = value.charAt(0);
        if (Character.isLetter(c)) {
            String altValue = value.replaceAll("['\"%]", "_");
            if (Character.isUpperCase(c)) {
                altValue = Character.toLowerCase(c) + value.substring(1);
            } else if (Character.isLowerCase(c)) {
                altValue = Character.toUpperCase(c) + value.substring(1);
            }
            MagicCardFilter.BinaryExpr b1 = MagicCardFilter.BinaryExpr.fieldLike(field, "%" + value + "%");
            MagicCardFilter.BinaryExpr b2 = MagicCardFilter.BinaryExpr.fieldLike(field, "%" + altValue + "%");
            MagicCardFilter.BinaryExpr res = new MagicCardFilter.BinaryExpr(b1, MagicCardFilter.Operation.OR, b2);
            return res;
        } else {
            return MagicCardFilter.BinaryExpr.fieldLike(field, "%" + value + "%");
        }
    }
    private int limit = Integer.MAX_VALUE;

    public static MagicCardFilter.BinaryExpr tokenSearch(ICardField field, MagicCardFilter.SearchToken token) {
        String value = token.getValue();
        if (token.getType() == TokenType.REGEX) {
            MagicCardFilter.TextValue tvalue = new MagicCardFilter.TextValue(value, false, false, true);
            return new MagicCardFilter.BinaryExpr(new MagicCardFilter.Field(field), MagicCardFilter.Operation.MATCHES, tvalue);
        } else {
            MagicCardFilter.TextValue tvalue = new MagicCardFilter.TextValue(value, false, false, false);
            char c = value.charAt(0);
            if (Character.isLetter(c) && token.getType() != TokenType.QUOTED) {
                tvalue.setWordBoundary(true);
            }
            return new MagicCardFilter.BinaryExpr(new MagicCardFilter.Field(field), MagicCardFilter.Operation.MATCHES, tvalue);
        }
    }
    private MagicSortOrder sortOrder = new MagicSortOrder();

    static public CardFilterExpr textSearch(ICardField field, String text) {
        MagicCardFilter.SearchStringTokenizer tokenizer = new MagicCardFilter.SearchStringTokenizer();
        tokenizer.init(text);
        MagicCardFilter.SearchToken token;
        CardFilterExpr res = null;
        while ((token = tokenizer.nextToken()) != null) {
            MagicCardFilter.BinaryExpr cur;
            if (token.getType() == TokenType.NOT) {
                token = tokenizer.nextToken();
                if (token == null) {
                    break;
                }
                cur = tokenSearch(field, token);
                cur = new MagicCardFilter.BinaryExpr(cur, MagicCardFilter.Operation.NOT, null);
            } else {
                cur = tokenSearch(field, token);
            }
            res = createAndGroup(res, cur);
        }
        return res;
    }
    private ICardField groupField = null;

    private static CardFilterExpr translate(MagicCardFilter.BinaryExpr bin) {
        CardFilterExpr res = bin;
        String requestedId = bin.getLeft().toString();
        String value = bin.getRight().toString();
        if (Colors.getInstance().getIdPrefix().equals(requestedId)) {
            String en = Colors.getInstance().getEncodeByName(value);
            if (en != null) {
                res = MagicCardFilter.BinaryExpr.fieldMatches(MagicCardField.COST, ".*" + en + ".*");
            } else if (value.equals("Multi-Color")) {
                res = MagicCardFilter.BinaryExpr.fieldEquals(MagicCardField.CTYPE, "multi");
            } else if (value.equals("Mono-Color")) {
                MagicCardFilter.BinaryExpr b1 = MagicCardFilter.BinaryExpr.fieldEquals(MagicCardField.CTYPE, "colorless");
                MagicCardFilter.BinaryExpr b2 = MagicCardFilter.BinaryExpr.fieldEquals(MagicCardField.CTYPE, "mono");
                res = new MagicCardFilter.BinaryExpr(b1, MagicCardFilter.Operation.OR, b2);
            } else if (value.equals("Hybrid")) {
                res = MagicCardFilter.BinaryExpr.fieldEquals(MagicCardField.CTYPE, "hybrid");
            } else if (value.equals("Colorless")) {
                MagicCardFilter.BinaryExpr b1 = MagicCardFilter.BinaryExpr.fieldEquals(MagicCardField.CTYPE, "colorless");
                MagicCardFilter.BinaryExpr b2 = MagicCardFilter.BinaryExpr.fieldEquals(MagicCardField.CTYPE, "land");
                res = new MagicCardFilter.BinaryExpr(b1, MagicCardFilter.Operation.OR, b2);
            }
        } else if (CardTypes.getInstance().getIdPrefix().equals(requestedId)) {
            res = textSearch(MagicCardField.TYPE, value);
        } else if (Editions.getInstance().getIdPrefix().equals(requestedId)) {
            res = MagicCardFilter.BinaryExpr.fieldEquals(MagicCardField.SET, value);
        } else if (SuperTypes.getInstance().getIdPrefix().equals(requestedId)) {
            MagicCardFilter.BinaryExpr b1 = MagicCardFilter.BinaryExpr.fieldMatches(MagicCardField.TYPE, ".*" + value + " .*");
            MagicCardFilter.BinaryExpr b2 = MagicCardFilter.BinaryExpr.fieldMatches(MagicCardField.TYPE, ".*" + value + " -.*");
            res = new MagicCardFilter.BinaryExpr(b1, MagicCardFilter.Operation.AND, new MagicCardFilter.BinaryExpr(b2, MagicCardFilter.Operation.NOT, null));
        } else if (FilterHelper.TYPE_LINE.equals(requestedId)) {
            res = textSearch(MagicCardField.TYPE, value);
        } else if (FilterHelper.NAME_LINE.equals(requestedId)) {
            res = textSearch(MagicCardField.NAME, value);
        } else if (FilterHelper.CCC.equals(requestedId)) {
            res = MagicCardFilter.BinaryExpr.fieldInt(MagicCardField.CMC, value);
        } else if (FilterHelper.POWER.equals(requestedId)) {
            res = MagicCardFilter.BinaryExpr.fieldInt(MagicCardField.POWER, value);
        } else if (FilterHelper.TOUGHNESS.equals(requestedId)) {
            res = MagicCardFilter.BinaryExpr.fieldInt(MagicCardField.TOUGHNESS, value);
        } else if (FilterHelper.RARITY.equals(requestedId)) {
            res = MagicCardFilter.BinaryExpr.fieldEquals(MagicCardField.RARITY, value);
        } else if (FilterHelper.COUNT.equals(requestedId)) {
            res = MagicCardFilter.BinaryExpr.fieldInt(MagicCardFieldPhysical.COUNT, value);
        } else if (MagicCardFieldPhysical.FORTRADECOUNT.name().equals(requestedId)) {
            res = MagicCardFilter.BinaryExpr.fieldInt(MagicCardFieldPhysical.FORTRADECOUNT, value);
        } else if (FilterHelper.DBPRICE.equals(requestedId)) {
            MagicCardFilter.BinaryExpr b1 = new MagicCardFilter.BinaryExpr(new MagicCardFilter.Field(MagicCardField.DBPRICE), MagicCardFilter.Operation.EQ, new MagicCardFilter.Value("0"));
            res = new MagicCardFilter.BinaryExpr(b1, MagicCardFilter.Operation.AND, MagicCardFilter.BinaryExpr.fieldInt(MagicCardFieldPhysical.PRICE, value));
            res = new MagicCardFilter.BinaryExpr(res, MagicCardFilter.Operation.OR, MagicCardFilter.BinaryExpr.fieldInt(MagicCardField.DBPRICE, value));
        } else if (FilterHelper.COMMUNITYRATING.equals(requestedId)) {
            res = MagicCardFilter.BinaryExpr.fieldInt(MagicCardField.RATING, value);
        } else if (FilterHelper.COLLNUM.equals(requestedId)) {
            res = MagicCardFilter.BinaryExpr.fieldInt(MagicCardField.COLLNUM, value);
        } else if (FilterHelper.ARTIST.equals(requestedId)) {
            res = textSearch(MagicCardField.ARTIST, value);
        } else if (FilterHelper.PRICE.equals(requestedId)) {
            MagicCardFilter.BinaryExpr b1 = new MagicCardFilter.BinaryExpr(new MagicCardFilter.Field(MagicCardFieldPhysical.PRICE), MagicCardFilter.Operation.EQ, new MagicCardFilter.Value("0"));
            res = new MagicCardFilter.BinaryExpr(b1, MagicCardFilter.Operation.AND, MagicCardFilter.BinaryExpr.fieldInt(MagicCardField.DBPRICE, value));
            res = new MagicCardFilter.BinaryExpr(res, MagicCardFilter.Operation.OR, MagicCardFilter.BinaryExpr.fieldInt(MagicCardFieldPhysical.PRICE, value));
        } else if (FilterHelper.COMMENT.equals(requestedId)) {
            res = textSearch(MagicCardFieldPhysical.COMMENT, value);
        } else if (MagicCardFieldPhysical.SPECIAL.name().equals(requestedId)) {
            res = textSearch(MagicCardFieldPhysical.SPECIAL, value);
        } else if (FilterHelper.OWNERSHIP.equals(requestedId)) {
            res = MagicCardFilter.BinaryExpr.fieldEquals(MagicCardFieldPhysical.OWNERSHIP, value);
        } else if (FilterHelper.LANG.equals(requestedId)) {
            if (value.isEmpty()) {
                res = TRUE;
            } else if (value.equals(Languages.Language.ENGLISH.getLang())) {
                res = MagicCardFilter.BinaryExpr.fieldEquals(MagicCardField.LANG, null);
                res = new MagicCardFilter.BinaryExpr(res, MagicCardFilter.Operation.OR, MagicCardFilter.BinaryExpr.fieldEquals(MagicCardField.LANG, value));
            } else {
                res = MagicCardFilter.BinaryExpr.fieldEquals(MagicCardField.LANG, value);
            }
        } else if (requestedId.startsWith(FilterHelper.TEXT_LINE)) {
            res = textSearch(MagicCardField.TEXT, value);
            res = new MagicCardFilter.BinaryExpr(res, MagicCardFilter.Operation.OR, textSearch(MagicCardField.ORACLE, value));
            if (requestedId.contains("_exclude_")) {
                res = new MagicCardFilter.BinaryExpr(res, MagicCardFilter.Operation.NOT, null);
            }
        } else {
            res = bin;
        }
        res.setTranslated(true);
        return res;
    }
    private boolean onlyLastSet = false;

    private static CardFilterExpr createAndGroup(CardFilterExpr first, CardFilterExpr expr) {
        if (expr == null) {
            return first;
        }
        if (first == null) {
            return expr;
        }
        MagicCardFilter.BinaryExpr and = new MagicCardFilter.BinaryExpr(first, MagicCardFilter.Operation.AND, expr);
        return and;
    }

    @Override
    public CardFilterExpr getRoot() {
        return this.root;
    }

    @Override
    public void update(HashMap map) {
        CardFilterExpr expr;
        if (map.containsKey(ColorTypes.AND_ID)) {
            map.remove(ColorTypes.AND_ID);
            expr = createAndGroup(map, Colors.getInstance());
        } else if (map.containsKey(ColorTypes.ONLY_ID)) {
            map.remove(ColorTypes.ONLY_ID);
            expr = createAndNotGroup(map, Colors.getInstance());
        } else {
            expr = createOrGroup(map, Colors.getInstance());
        }
        expr = createAndGroup(createOrGroup(map, ColorTypes.getInstance()), expr);
        expr = createAndGroup(createOrGroup(map, CardTypes.getInstance()), expr);
        expr = createAndGroup(createOrGroup(map, SuperTypes.getInstance()), expr);
        expr = createAndGroup(createOrGroup(map, Editions.getInstance()), expr);
        expr = createAndGroup(createOrGroup(map, Rarity.getInstance()), expr);
        expr = createAndGroup(createTextSearch(map, FilterHelper.LANG), expr);
        expr = createAndGroup(createTextSearch(map, FilterHelper.TYPE_LINE), expr);
        expr = createAndGroup(createTextSearch(map, FilterHelper.NAME_LINE), expr);
        expr = createAndGroup(createNumericSearch(map, FilterHelper.POWER), expr);
        expr = createAndGroup(createNumericSearch(map, FilterHelper.TOUGHNESS), expr);
        expr = createAndGroup(createNumericSearch(map, FilterHelper.CCC), expr);
        expr = createAndGroup(createNumericSearch(map, FilterHelper.COUNT), expr);
        expr = createAndGroup(createNumericSearch(map, FilterHelper.PRICE), expr);
        expr = createAndGroup(createNumericSearch(map, FilterHelper.DBPRICE), expr);
        expr = createAndGroup(createTextSearch(map, FilterHelper.COMMENT), expr);
        expr = createAndGroup(createTextSearch(map, FilterHelper.OWNERSHIP), expr);
        expr = createAndGroup(createNumericSearch(map, FilterHelper.COMMUNITYRATING), expr);
        expr = createAndGroup(createNumericSearch(map, FilterHelper.COLLNUM), expr);
        expr = createAndGroup(createTextSearch(map, FilterHelper.ARTIST), expr);
        expr = createAndGroup(createTextSearch(map, MagicCardFieldPhysical.SPECIAL.name()), expr);
        expr = createAndGroup(createNumericSearch(map, MagicCardFieldPhysical.FORTRADECOUNT.name()), expr);
        // text fields
        CardFilterExpr text = createTextSearch(map, FilterHelper.TEXT_LINE);
        text = createOrGroup(text, createTextSearch(map, FilterHelper.TEXT_LINE_2));
        text = createOrGroup(text, createTextSearch(map, FilterHelper.TEXT_LINE_3));
        expr = createAndGroup(expr, text);
        expr = createAndGroup(createTextSearch(map, FilterHelper.TEXT_NOT_1), expr);
        expr = createAndGroup(createTextSearch(map, FilterHelper.TEXT_NOT_2), expr);
        expr = createAndGroup(createTextSearch(map, FilterHelper.TEXT_NOT_3), expr);
        this.root = expr;
    }

    public static class Node extends CardFilterExpr {

        private String name;

        Node(String name) {
            this.name = name;
        }

        @Override
        public boolean evaluate(Object o) {
            return true;
        }

        @Override
        public String toString() {
            return this.getName();
        }

        @Override
        public Object getFieldValue(ICardField o) {
            return this.getName();
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }
    }

    private CardFilterExpr createTextSearch(Map map, String fieldId) {
        CardFilterExpr sub = null;
        String valueKey = FilterHelper.getPrefConstant(fieldId, FilterHelper.TEXT_POSTFIX);
        String value = (String) map.get(valueKey);
        if (value != null && value.length() > 0) {
            sub = new MagicCardFilter.BinaryExpr(new MagicCardFilter.Node(fieldId), MagicCardFilter.Operation.EQUALS, new MagicCardFilter.Node(value));
        }
        return sub;
    }

    public static class Field extends CardFilterExpr {

        private ICardField field;

        Field(ICardField field) {
            this.field = field;
        }

        @Override
        public boolean evaluate(Object o) {
            return true;
        }

        @Override
        public String toString() {
            return "f" + this.getField();
        }

        @Override
        public Object getFieldValue(ICardField o) {
            if (o instanceof IMagicCard) {
                return ((IMagicCard) o).getObjectByField(getField());
            }
            return null;
        }

        /**
         * @return the field
         */
        public ICardField getField() {
            return field;
        }

        /**
         * @param field the field to set
         */
        public void setField(ICardField field) {
            this.field = field;
        }
    }

    private CardFilterExpr createNumericSearch(Map map, String fieldId) {
        CardFilterExpr sub = null;
        String valueKey = FilterHelper.getPrefConstant(fieldId, FilterHelper.NUMERIC_POSTFIX);
        String value = (String) map.get(valueKey);
        if (value != null && value.length() > 0) {
            sub = new MagicCardFilter.BinaryExpr(new MagicCardFilter.Node(fieldId), MagicCardFilter.Operation.EQUALS, new MagicCardFilter.Node(value));
        }
        return sub;
    }

    static class Value extends MagicCardFilter.Node {

        Value(String name) {
            super(name);
        }

        @Override
        public String toString() {
            return "'" + this.getName() + "'";
        }
    }

    private CardFilterExpr createOrGroup(CardFilterExpr or, CardFilterExpr res) {
        if (res == null) {
            res = or;
        } else if (or != null) {
            res = new MagicCardFilter.BinaryExpr(or, MagicCardFilter.Operation.OR, res);
        }
        return res;
    }

    public static class TextValue extends MagicCardFilter.Value {

        private boolean wordBoundary = true;
        private boolean caseSensitive = false;
        private boolean regex = false;

        public TextValue(String name, boolean wordBoundary,
                boolean caseSensitive, boolean regex) {
            super(name);
            this.wordBoundary = wordBoundary;
            this.caseSensitive = caseSensitive;
            this.regex = regex;
        }

        public void setWordBoundary(boolean b) {
            this.wordBoundary = b;
        }

        public Pattern toPattern() {
            if (isRegex()) {
                return Pattern.compile(getName());
            }
            int flags = 0;
            if (!isCaseSensitive()) {
                flags |= Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;
            }
            if (isWordBoundary()) {
                return Pattern.compile("\\b\\Q" + getName() + "\\E\\b", flags);
            }
            flags |= Pattern.LITERAL;
            return Pattern.compile(getName(), flags);
        }

        public String getText() {
            return getName();
        }

        /**
         * @return the wordBoundary
         */
        public boolean isWordBoundary() {
            return wordBoundary;
        }

        /**
         * @return the caseSensitive
         */
        public boolean isCaseSensitive() {
            return caseSensitive;
        }

        /**
         * @param caseSensitive the caseSensitive to set
         */
        public void setCaseSensitive(boolean caseSensitive) {
            this.caseSensitive = caseSensitive;
        }

        /**
         * @return the regex
         */
        public boolean isRegex() {
            return regex;
        }

        /**
         * @param regex the regex to set
         */
        public void setRegex(boolean regex) {
            this.regex = regex;
        }
    }

    private CardFilterExpr createOrGroup(Map map, ISearchableProperty sp) {
        return createGroup(map, sp, true, false);
    }

    public static class BinaryExpr extends CardFilterExpr {

        CardFilterExpr left;
        CardFilterExpr right;
        MagicCardFilter.Operation op;

        BinaryExpr(CardFilterExpr left, MagicCardFilter.Operation op, CardFilterExpr right) {
            this.left = left;
            this.right = right;
            this.op = op;
        }

        @Override
        public String toString() {
            if (this.op == MagicCardFilter.Operation.NOT) {
                return this.op + " (" + this.left + ")";
            }
            return this.left + " " + this.op + " " + this.right;
        }

        public CardFilterExpr getLeft() {
            return this.left;
        }

        public CardFilterExpr getRight() {
            return this.right;
        }

        public MagicCardFilter.Operation getOp() {
            return this.op;
        }

        public static MagicCardFilter.BinaryExpr fieldEquals(ICardField field, String value) {
            return new MagicCardFilter.BinaryExpr(new MagicCardFilter.Field(field), MagicCardFilter.Operation.EQUALS, new MagicCardFilter.Value(value));
        }

        public static MagicCardFilter.BinaryExpr fieldMatches(ICardField field, String value) {
            return new MagicCardFilter.BinaryExpr(new MagicCardFilter.Field(field), MagicCardFilter.Operation.MATCHES, new MagicCardFilter.Value(value));
        }

        public static MagicCardFilter.BinaryExpr fieldLike(ICardField field, String value) {
            return new MagicCardFilter.BinaryExpr(new MagicCardFilter.Field(field), MagicCardFilter.Operation.LIKE, new MagicCardFilter.Value(value));
        }

        public static MagicCardFilter.BinaryExpr fieldOp(ICardField field, MagicCardFilter.Operation op, String value) {
            return new MagicCardFilter.BinaryExpr(new MagicCardFilter.Field(field), op, new MagicCardFilter.Value(value));
        }

        @Override
        public boolean evaluate(Object o) {
            if (this.op == MagicCardFilter.Operation.AND) {
                boolean res = this.left.evaluate(o);
                if (res == false) {
                    return false;
                }
                return this.right.evaluate(o);
            } else if (this.op == MagicCardFilter.Operation.OR) {
                boolean res = this.left.evaluate(o);
                if (res == true) {
                    return true;
                }
                return this.right.evaluate(o);
            } else if (this.op == MagicCardFilter.Operation.NOT) {
                boolean res = this.left.evaluate(o);
                return !res;
            }
            if (!this.isTranslated()) {
                return translate(this).evaluate(o);
            }
            if (this.op == MagicCardFilter.Operation.EQUALS) {
                Object x = this.left.getFieldValue((ICardField) o);
                Object y = this.right.getFieldValue((ICardField) o);
                if (x == null && y == null) {
                    return true;
                }
                if (x == null || y == null) {
                    return false;
                }
                if (x instanceof String && y instanceof String) {
                    return x.equals(y);
                } else {
                    return x.toString().equals(y.toString());
                }
            } else if (this.op == MagicCardFilter.Operation.MATCHES) {
                return evalutateMatches(o);
            } else if (this.op == MagicCardFilter.Operation.LIKE) {
                return true; // processed by DB
            } else if (this.op == MagicCardFilter.Operation.EQ
                    || this.op == MagicCardFilter.Operation.LE
                    || this.op == MagicCardFilter.Operation.GE) {
                Object x = this.left.getFieldValue((ICardField) o);
                Object y = this.right.getFieldValue((ICardField) o);
                if (x == null && y == null) {
                    return true;
                }
                if (x == null || x.equals("")) {
                    x = "0";
                }
                if (y == null || y.equals("")) {
                    y = "0";
                }
                if (x.equals(y)) {
                    return true;
                }
                String sx = x.toString();
                String sy = y.toString();
                try {
                    float dx = Float.parseFloat(sx);
                    float dy = Float.parseFloat(sy);
                    if (this.op == MagicCardFilter.Operation.EQ) {
                        return Float.compare(dx, dy) == 0;
                    }
                    if (this.op == MagicCardFilter.Operation.GE) {
                        return dx >= dy;
                    }
                    if (this.op == MagicCardFilter.Operation.LE) {
                        return dx <= dy;
                    }
                } catch (NumberFormatException e) {
                    return false;
                }
                return false;
            }
            return true;
        }

        boolean evalutateMatches(Object o) {
            Object x = this.left.getFieldValue((ICardField) o);
            Object y = this.right.getFieldValue((ICardField) o);
            if (x == null && y == null) {
                return true;
            }
            if (x == null || y == null) {
                return false;
            }
            if (this.left instanceof MagicCardFilter.Field
                    && o instanceof IMagicCard
                    && this.right instanceof MagicCardFilter.TextValue) {
                return ((IMagicCard) o).matches(((MagicCardFilter.Field) this.left).getField(), (MagicCardFilter.TextValue) this.right);
            }
            if (x instanceof String && y instanceof String) {
                String pattern = (String) y;
                String text = (String) x;
                return Pattern.compile(pattern).matcher(text).find();
            }
            return false;
        }

        public static CardFilterExpr fieldInt(ICardField field, String value) {
            if (value.equals(">= 0")) {
                return TRUE;
            } else if (value.startsWith(">=")) {
                return fieldOp(field, MagicCardFilter.Operation.GE, value.substring(2).trim());
            } else if (value.startsWith("<=")) {
                return fieldOp(field, MagicCardFilter.Operation.LE, value.substring(2).trim());
            } else if (value.startsWith("=")) {
                return fieldOp(field, MagicCardFilter.Operation.EQ, value.substring(2).trim());
            } else if (value.equals("0")) {
                return TRUE;
            }
            return null;
        }
    }

    private CardFilterExpr createAndGroup(Map map, ISearchableProperty sp) {
        return createGroup(map, sp, false, false);
    }

    public static class SearchToken {

        private TokenType type;

        public TokenType getType() {
            return type;
        }

        public static enum TokenType {

            WORD,
            QUOTED,
            REGEX,
            NOT;
        }
        private String value;

        ;

        public String getValue() {
            return value;
        }

        SearchToken(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }
    }

    private CardFilterExpr createAndNotGroup(Map map, ISearchableProperty sp) {
        return createGroup(map, sp, false, true);
    }

    public static class SearchStringTokenizer {

        static enum State {

            INIT,
            IN_QUOTE,
            IN_REG
        };
        private CharSequence seq;
        private int cur;
        private MagicCardFilter.SearchStringTokenizer.State state;

        public void init(CharSequence seq) {
            this.seq = seq;
            this.cur = 0;
            this.state = MagicCardFilter.SearchStringTokenizer.State.INIT;
        }
        boolean tokenReady = false;
        StringBuffer str;
        MagicCardFilter.SearchToken token = null;

        public MagicCardFilter.SearchToken nextToken() {
            tokenReady = false;
            str = new StringBuffer();
            token = null;
            while (tokenReady == false && cur <= seq.length()) {
                char c = cur < seq.length() ? seq.charAt(cur) : 0;
                switch (state) {
                    case INIT:
                        switch (c) {
                            case '"':
                                pushToken(TokenType.WORD);
                                state = MagicCardFilter.SearchStringTokenizer.State.IN_QUOTE;
                                break;
                            case 'm':
                                if (cur + 1 < seq.length() && seq.charAt(cur + 1) == '/') {
                                    pushToken(TokenType.WORD);
                                    state = MagicCardFilter.SearchStringTokenizer.State.IN_REG;
                                    cur++;
                                } else {
                                    str.append(c);
                                }
                                break;
                            case '-':
                                pushToken(TokenType.WORD);
                                str.append('-');
                                pushToken(TokenType.NOT);
                                break;
                            case ' ':
                            case 0:
                                pushToken(TokenType.WORD);
                                break;
                            default:
                                str.append(c);
                                break;
                        }
                        break;
                    case IN_REG:
                        if (c == '/' || c == 0) {
                            pushToken(TokenType.REGEX);
                            state = MagicCardFilter.SearchStringTokenizer.State.INIT;
                        } else {
                            str.append(c);
                        }
                        break;
                    case IN_QUOTE:
                        if (c == '"' || c == 0) {
                            pushToken(TokenType.QUOTED);
                            state = MagicCardFilter.SearchStringTokenizer.State.INIT;
                        } else {
                            str.append(c);
                        }
                        break;
                }
                cur++;
            }
            return token;
        }

        private void pushToken(TokenType type) {
            if (str.length() > 0) {
                token = new MagicCardFilter.SearchToken(type, str.toString());
                str.delete(0, str.length());
                tokenReady = true;
            }
        }
    }
    private CardFilterExpr createGroup(Map map, ISearchableProperty sp,
            boolean orOp, boolean notOp) {
        CardFilterExpr res = null;
        for (String id : sp.getIds()) {
            String value = (String) map.get(id);
            MagicCardFilter.BinaryExpr or = null;
            if (value != null && value.equals("true")) {
                or = new MagicCardFilter.BinaryExpr(
                        new MagicCardFilter.Node(sp.getIdPrefix()),
                        MagicCardFilter.Operation.EQUALS,
                        new MagicCardFilter.Node(sp.getNameById(id)));
            } else if (value == null || value.equals("false")) {
                if (notOp) {
                    or = new MagicCardFilter.BinaryExpr(
                            new MagicCardFilter.Node(sp.getIdPrefix()),
                            MagicCardFilter.Operation.EQUALS,
                            new MagicCardFilter.Node(sp.getNameById(id)));
                    or = new MagicCardFilter.BinaryExpr(or,
                            MagicCardFilter.Operation.NOT, null);
                } else {
                    // skip or = null;
                }
            } else if (value.length() > 0) {
                or = new MagicCardFilter.BinaryExpr(
                        new MagicCardFilter.Node(sp.getIdPrefix()),
                        MagicCardFilter.Operation.EQUALS,
                        new MagicCardFilter.Value(value));
            }
            if (or == null) {
                continue;
            }
            if (orOp) {
                res = createOrGroup(or, res);
            } else {
                res = createAndGroup(or, res);
            }
        }
        return res;
    }

    public static class Operation {

        public static final MagicCardFilter.Operation AND = new MagicCardFilter.Operation("AND");
        public static final MagicCardFilter.Operation OR = new MagicCardFilter.Operation("OR");
        public static final MagicCardFilter.Operation EQUALS = new MagicCardFilter.Operation("eq");
        public static final MagicCardFilter.Operation MATCHES = new MagicCardFilter.Operation("matches");
        public static final MagicCardFilter.Operation NOT = new MagicCardFilter.Operation("NOT");
        public static final MagicCardFilter.Operation GE = new MagicCardFilter.Operation(">=");
        public static final MagicCardFilter.Operation LE = new MagicCardFilter.Operation("<=");
        public static final MagicCardFilter.Operation EQ = new MagicCardFilter.Operation("==");
        public static final MagicCardFilter.Operation LIKE = new MagicCardFilter.Operation("LIKE");
        private String name;

        Operation(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    @Override
    public int getLimit() {
        return this.limit;
    }

    /**
     * sort field
     *
     * @param sortField
     * @param accending
     */
    @Override
    public void setSortField(ICardField sortField, boolean accending) {
        sortOrder.setSortField(sortField, accending);
    }

    @Override
    public AbstractSortOrder getSortOrder() {
        return this.sortOrder;
    }

    @Override
    public ICardField getGroupField() {
        return this.groupField;
    }

    @Override
    public void setGroupField(ICardField groupField) {
        this.groupField = groupField;
    }

    @Override
    public void setLimit(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Invalid value for limit (must be >=0)");
        }
        this.limit = limit;
    }

    @Override
    public boolean isFiltered(Object o) {
        if (this.root == null) {
            return false;
        }
        boolean res = !this.root.evaluate(o);
        return res;
    }

    @Override
    public boolean isOnlyLastSet() {
        return onlyLastSet;
    }

    @Override
    public void setOnlyLastSet(boolean onlyLastSet) {
        this.onlyLastSet = onlyLastSet;
    }

    @Override
    public void setNoSort() {
        sortOrder.clear();
    }
}
