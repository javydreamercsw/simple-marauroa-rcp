package mtg.card;

import com.reflexit.magiccards.core.model.ICard;
import com.reflexit.magiccards.core.model.ICardField;
import com.reflexit.magiccards.core.model.ISellableCard;
import mtg.card.MagicCardFilter.TextValue;

public interface IMagicCard extends ISellableCard {

    public static final MagicCard DEFAULT = new MagicCard();
    public static final float STAR_POWER = 911.0F;
    public static final float NOT_APPLICABLE_POWER = Float.NaN;

    public abstract String getCost();

    public abstract String getOracleText();

    public abstract String getRarity();

    public abstract String getType();

    public abstract String getPower();

    public abstract String getToughness();

    public abstract String getColorType();

    public abstract int getCmc();

    public abstract float getCommunityRating();

    public abstract String getArtist();

    public abstract String getRulings();

    public ICard cloneCard();

    public abstract MagicCard getBase();

    public abstract String getText();

    public abstract String getLanguage();

    public abstract int getEnglishCardId();

    public abstract int getFlipId();
    
    public abstract boolean matches(ICardField left, TextValue right);
}