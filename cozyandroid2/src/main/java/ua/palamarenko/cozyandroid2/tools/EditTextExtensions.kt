package ua.palamarenko.cozyandroid2.tools

import android.content.Context
import android.widget.EditText
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import ru.tinkoff.decoro.FormattedTextChangeListener
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.parser.PhoneNumberUnderscoreSlotsParser
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher
import java.util.*

val text = "[\n" +
        "  { \"mask\": \"+247-####\", \"cc\": \"AC\", \"name_en\": \"Ascension\", \"desc_en\": \"\", \"name_ru\": \"Остров Вознесения\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+376-###-###\", \"cc\": \"AD\", \"name_en\": \"Andorra\", \"desc_en\": \"\", \"name_ru\": \"Андорра\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+971-5#-###-####\", \"cc\": \"AE\", \"name_en\": \"United Arab Emirates\", \"desc_en\": \"mobile\", \"name_ru\": \"Объединенные Арабские Эмираты\", \"desc_ru\": \"мобильные\" },\n" +
        "  { \"mask\": \"+971-#-###-####\", \"cc\": \"AE\", \"name_en\": \"United Arab Emirates\", \"desc_en\": \"\", \"name_ru\": \"Объединенные Арабские Эмираты\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+93-##-###-####\", \"cc\": \"AF\", \"name_en\": \"Afghanistan\", \"desc_en\": \"\", \"name_ru\": \"Афганистан\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+1(268)###-####\", \"cc\": \"AG\", \"name_en\": \"Antigua & Barbuda\", \"desc_en\": \"\", \"name_ru\": \"Антигуа и Барбуда\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+1(264)###-####\", \"cc\": \"AI\", \"name_en\": \"Anguilla\", \"desc_en\": \"\", \"name_ru\": \"Ангилья\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+355(###)###-###\", \"cc\": \"AL\", \"name_en\": \"Albania\", \"desc_en\": \"\", \"name_ru\": \"Албания\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+374-##-###-###\", \"cc\": \"AM\", \"name_en\": \"Armenia\", \"desc_en\": \"\", \"name_ru\": \"Армения\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+599-###-####\", \"cc\": \"AN\", \"name_en\": \"Caribbean Netherlands\", \"desc_en\": \"\", \"name_ru\": \"Карибские Нидерланды\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+599-###-####\", \"cc\": \"AN\", \"name_en\": \"Netherlands Antilles\", \"desc_en\": \"\", \"name_ru\": \"Нидерландские Антильские острова\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+599-9###-####\", \"cc\": \"AN\", \"name_en\": \"Netherlands Antilles\", \"desc_en\": \"Curacao\", \"name_ru\": \"Нидерландские Антильские острова\", \"desc_ru\": \"Кюрасао\" },\n" +
        "  { \"mask\": \"+244(###)###-###\", \"cc\": \"AO\", \"name_en\": \"Angola\", \"desc_en\": \"\", \"name_ru\": \"Ангола\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+672-1##-###\", \"cc\": \"AQ\", \"name_en\": \"Australian bases in Antarctica\", \"desc_en\": \"\", \"name_ru\": \"Австралийская антарктическая база\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+54(###)###-####\", \"cc\": \"AR\", \"name_en\": \"Argentina\", \"desc_en\": \"\", \"name_ru\": \"Аргентина\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+1(684)###-####\", \"cc\": \"AS\", \"name_en\": \"American Samoa\", \"desc_en\": \"\", \"name_ru\": \"Американское Самоа\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+43(###)###-####\", \"cc\": \"AT\", \"name_en\": \"Austria\", \"desc_en\": \"\", \"name_ru\": \"Австрия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+61-#-####-####\", \"cc\": \"AU\", \"name_en\": \"Australia\", \"desc_en\": \"\", \"name_ru\": \"Австралия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+297-###-####\", \"cc\": \"AW\", \"name_en\": \"Aruba\", \"desc_en\": \"\", \"name_ru\": \"Аруба\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+994-##-###-##-##\", \"cc\": \"AZ\", \"name_en\": \"Azerbaijan\", \"desc_en\": \"\", \"name_ru\": \"Азербайджан\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+387-##-#####\", \"cc\": \"BA\", \"name_en\": \"Bosnia and Herzegovina\", \"desc_en\": \"\", \"name_ru\": \"Босния и Герцеговина\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+387-##-####\", \"cc\": \"BA\", \"name_en\": \"Bosnia and Herzegovina\", \"desc_en\": \"\", \"name_ru\": \"Босния и Герцеговина\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+1(246)###-####\", \"cc\": \"BB\", \"name_en\": \"Barbados\", \"desc_en\": \"\", \"name_ru\": \"Барбадос\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+880-##-###-###\", \"cc\": \"BD\", \"name_en\": \"Bangladesh\", \"desc_en\": \"\", \"name_ru\": \"Бангладеш\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+32(###)###-###\", \"cc\": \"BE\", \"name_en\": \"Belgium\", \"desc_en\": \"\", \"name_ru\": \"Бельгия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+226-##-##-####\", \"cc\": \"BF\", \"name_en\": \"Burkina Faso\", \"desc_en\": \"\", \"name_ru\": \"Буркина Фасо\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+359(###)###-###\", \"cc\": \"BG\", \"name_en\": \"Bulgaria\", \"desc_en\": \"\", \"name_ru\": \"Болгария\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+973-####-####\", \"cc\": \"BH\", \"name_en\": \"Bahrain\", \"desc_en\": \"\", \"name_ru\": \"Бахрейн\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+257-##-##-####\", \"cc\": \"BI\", \"name_en\": \"Burundi\", \"desc_en\": \"\", \"name_ru\": \"Бурунди\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+229-##-##-####\", \"cc\": \"BJ\", \"name_en\": \"Benin\", \"desc_en\": \"\", \"name_ru\": \"Бенин\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+1(441)###-####\", \"cc\": \"BM\", \"name_en\": \"Bermuda\", \"desc_en\": \"\", \"name_ru\": \"Бермудские острова\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+673-###-####\", \"cc\": \"BN\", \"name_en\": \"Brunei Darussalam\", \"desc_en\": \"\", \"name_ru\": \"Бруней-Даруссалам\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+591-#-###-####\", \"cc\": \"BO\", \"name_en\": \"Bolivia\", \"desc_en\": \"\", \"name_ru\": \"Боливия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+55(##)####-####\", \"cc\": \"BR\", \"name_en\": \"Brazil\", \"desc_en\": \"\", \"name_ru\": \"Бразилия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+55(##)7###-####\", \"cc\": \"BR\", \"name_en\": \"Brazil\", \"desc_en\": \"mobile\", \"name_ru\": \"Бразилия\", \"desc_ru\": \"мобильные\" },\n" +
        "  { \"mask\": \"+55(##)9####-####\", \"cc\": \"BR\", \"name_en\": \"Brazil\", \"desc_en\": \"mobile\", \"name_ru\": \"Бразилия\", \"desc_ru\": \"мобильные\" },\n" +
        "  { \"mask\": \"+1(242)###-####\", \"cc\": \"BS\", \"name_en\": \"Bahamas\", \"desc_en\": \"\", \"name_ru\": \"Багамские Острова\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+975-17-###-###\", \"cc\": \"BT\", \"name_en\": \"Bhutan\", \"desc_en\": \"\", \"name_ru\": \"Бутан\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+975-#-###-###\", \"cc\": \"BT\", \"name_en\": \"Bhutan\", \"desc_en\": \"\", \"name_ru\": \"Бутан\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+267-##-###-###\", \"cc\": \"BW\", \"name_en\": \"Botswana\", \"desc_en\": \"\", \"name_ru\": \"Ботсвана\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+375(##)###-##-##\", \"cc\": \"BY\", \"name_en\": \"Belarus\", \"desc_en\": \"\", \"name_ru\": \"Беларусь (Белоруссия)\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+501-###-####\", \"cc\": \"BZ\", \"name_en\": \"Belize\", \"desc_en\": \"\", \"name_ru\": \"Белиз\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+243(###)###-###\", \"cc\": \"CD\", \"name_en\": \"Dem. Rep. Congo\", \"desc_en\": \"\", \"name_ru\": \"Дем. Респ. Конго (Киншаса)\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+236-##-##-####\", \"cc\": \"CF\", \"name_en\": \"Central African Republic\", \"desc_en\": \"\", \"name_ru\": \"Центральноафриканская Республика\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+242-##-###-####\", \"cc\": \"CG\", \"name_en\": \"Congo (Brazzaville)\", \"desc_en\": \"\", \"name_ru\": \"Конго (Браззавиль)\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+41-##-###-####\", \"cc\": \"CH\", \"name_en\": \"Switzerland\", \"desc_en\": \"\", \"name_ru\": \"Швейцария\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+225-##-###-###\", \"cc\": \"CI\", \"name_en\": \"Cote d’Ivoire (Ivory Coast)\", \"desc_en\": \"\", \"name_ru\": \"Кот-д’Ивуар\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+682-##-###\", \"cc\": \"CK\", \"name_en\": \"Cook Islands\", \"desc_en\": \"\", \"name_ru\": \"Острова Кука\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+56-#-####-####\", \"cc\": \"CL\", \"name_en\": \"Chile\", \"desc_en\": \"\", \"name_ru\": \"Чили\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+237-####-####\", \"cc\": \"CM\", \"name_en\": \"Cameroon\", \"desc_en\": \"\", \"name_ru\": \"Камерун\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+86(###)####-####\", \"cc\": \"CN\", \"name_en\": \"China (PRC)\", \"desc_en\": \"\", \"name_ru\": \"Китайская Н.Р.\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+86(###)####-###\", \"cc\": \"CN\", \"name_en\": \"China (PRC)\", \"desc_en\": \"\", \"name_ru\": \"Китайская Н.Р.\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+86-##-#####-#####\", \"cc\": \"CN\", \"name_en\": \"China (PRC)\", \"desc_en\": \"\", \"name_ru\": \"Китайская Н.Р.\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+57(###)###-####\", \"cc\": \"CO\", \"name_en\": \"Colombia\", \"desc_en\": \"\", \"name_ru\": \"Колумбия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+506-####-####\", \"cc\": \"CR\", \"name_en\": \"Costa Rica\", \"desc_en\": \"\", \"name_ru\": \"Коста-Рика\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+53-#-###-####\", \"cc\": \"CU\", \"name_en\": \"Cuba\", \"desc_en\": \"\", \"name_ru\": \"Куба\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+238(###)##-##\", \"cc\": \"CV\", \"name_en\": \"Cape Verde\", \"desc_en\": \"\", \"name_ru\": \"Кабо-Верде\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+599-###-####\", \"cc\": \"CW\", \"name_en\": \"Curacao\", \"desc_en\": \"\", \"name_ru\": \"Кюрасао\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+357-##-###-###\", \"cc\": \"CY\", \"name_en\": \"Cyprus\", \"desc_en\": \"\", \"name_ru\": \"Кипр\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+420(###)###-###\", \"cc\": \"CZ\", \"name_en\": \"Czech Republic\", \"desc_en\": \"\", \"name_ru\": \"Чехия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+49(####)###-####\", \"cc\": \"DE\", \"name_en\": \"Germany\", \"desc_en\": \"\", \"name_ru\": \"Германия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+49(###)###-####\", \"cc\": \"DE\", \"name_en\": \"Germany\", \"desc_en\": \"\", \"name_ru\": \"Германия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+49(###)##-####\", \"cc\": \"DE\", \"name_en\": \"Germany\", \"desc_en\": \"\", \"name_ru\": \"Германия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+49(###)##-###\", \"cc\": \"DE\", \"name_en\": \"Germany\", \"desc_en\": \"\", \"name_ru\": \"Германия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+49(###)##-##\", \"cc\": \"DE\", \"name_en\": \"Germany\", \"desc_en\": \"\", \"name_ru\": \"Германия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+49-###-###\", \"cc\": \"DE\", \"name_en\": \"Germany\", \"desc_en\": \"\", \"name_ru\": \"Германия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+253-##-##-##-##\", \"cc\": \"DJ\", \"name_en\": \"Djibouti\", \"desc_en\": \"\", \"name_ru\": \"Джибути\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+45-##-##-##-##\", \"cc\": \"DK\", \"name_en\": \"Denmark\", \"desc_en\": \"\", \"name_ru\": \"Дания\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+1(767)###-####\", \"cc\": \"DM\", \"name_en\": \"Dominica\", \"desc_en\": \"\", \"name_ru\": \"Доминика\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+1(809)###-####\", \"cc\": \"DO\", \"name_en\": \"Dominican Republic\", \"desc_en\": \"\", \"name_ru\": \"Доминиканская Республика\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+1(829)###-####\", \"cc\": \"DO\", \"name_en\": \"Dominican Republic\", \"desc_en\": \"\", \"name_ru\": \"Доминиканская Республика\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+1(849)###-####\", \"cc\": \"DO\", \"name_en\": \"Dominican Republic\", \"desc_en\": \"\", \"name_ru\": \"Доминиканская Республика\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+213-##-###-####\", \"cc\": \"DZ\", \"name_en\": \"Algeria\", \"desc_en\": \"\", \"name_ru\": \"Алжир\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+593-##-###-####\", \"cc\": \"EC\", \"name_en\": \"Ecuador \", \"desc_en\": \"mobile\", \"name_ru\": \"Эквадор \", \"desc_ru\": \"мобильные\" },\n" +
        "  { \"mask\": \"+593-#-###-####\", \"cc\": \"EC\", \"name_en\": \"Ecuador\", \"desc_en\": \"\", \"name_ru\": \"Эквадор\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+372-####-####\", \"cc\": \"EE\", \"name_en\": \"Estonia\", \"desc_en\": \"mobile\", \"name_ru\": \"Эстония \", \"desc_ru\": \"мобильные\" },\n" +
        "  { \"mask\": \"+372-###-####\", \"cc\": \"EE\", \"name_en\": \"Estonia\", \"desc_en\": \"\", \"name_ru\": \"Эстония\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+20(###)###-####\", \"cc\": \"EG\", \"name_en\": \"Egypt\", \"desc_en\": \"\", \"name_ru\": \"Египет\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+291-#-###-###\", \"cc\": \"ER\", \"name_en\": \"Eritrea\", \"desc_en\": \"\", \"name_ru\": \"Эритрея\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+34(###)###-###\", \"cc\": \"ES\", \"name_en\": \"Spain\", \"desc_en\": \"\", \"name_ru\": \"Испания\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+251-##-###-####\", \"cc\": \"ET\", \"name_en\": \"Ethiopia\", \"desc_en\": \"\", \"name_ru\": \"Эфиопия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+358(###)###-##-##\", \"cc\": \"FI\", \"name_en\": \"Finland\", \"desc_en\": \"\", \"name_ru\": \"Финляндия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+679-##-#####\", \"cc\": \"FJ\", \"name_en\": \"Fiji\", \"desc_en\": \"\", \"name_ru\": \"Фиджи\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+500-#####\", \"cc\": \"FK\", \"name_en\": \"Falkland Islands\", \"desc_en\": \"\", \"name_ru\": \"Фолклендские острова\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+691-###-####\", \"cc\": \"FM\", \"name_en\": \"F.S. Micronesia\", \"desc_en\": \"\", \"name_ru\": \"Ф.Ш. Микронезии\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+298-###-###\", \"cc\": \"FO\", \"name_en\": \"Faroe Islands\", \"desc_en\": \"\", \"name_ru\": \"Фарерские острова\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+262-#####-####\", \"cc\": \"FR\", \"name_en\": \"Mayotte\", \"desc_en\": \"\", \"name_ru\": \"Майотта\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+33(###)###-###\", \"cc\": \"FR\", \"name_en\": \"France\", \"desc_en\": \"\", \"name_ru\": \"Франция\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+508-##-####\", \"cc\": \"FR\", \"name_en\": \"St Pierre & Miquelon\", \"desc_en\": \"\", \"name_ru\": \"Сен-Пьер и Микелон\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+590(###)###-###\", \"cc\": \"FR\", \"name_en\": \"Guadeloupe\", \"desc_en\": \"\", \"name_ru\": \"Гваделупа\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+241-#-##-##-##\", \"cc\": \"GA\", \"name_en\": \"Gabon\", \"desc_en\": \"\", \"name_ru\": \"Габон\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+1(473)###-####\", \"cc\": \"GD\", \"name_en\": \"Grenada\", \"desc_en\": \"\", \"name_ru\": \"Гренада\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+995(###)###-###\", \"cc\": \"GE\", \"name_en\": \"Georgia\", \"desc_en\": \"\", \"name_ru\": \"Грузия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+594-#####-####\", \"cc\": \"GF\", \"name_en\": \"Guiana (French)\", \"desc_en\": \"\", \"name_ru\": \"Фр. Гвиана\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+233(###)###-###\", \"cc\": \"GH\", \"name_en\": \"Ghana\", \"desc_en\": \"\", \"name_ru\": \"Гана\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+350-###-#####\", \"cc\": \"GI\", \"name_en\": \"Gibraltar\", \"desc_en\": \"\", \"name_ru\": \"Гибралтар\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+299-##-##-##\", \"cc\": \"GL\", \"name_en\": \"Greenland\", \"desc_en\": \"\", \"name_ru\": \"Гренландия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+220(###)##-##\", \"cc\": \"GM\", \"name_en\": \"Gambia\", \"desc_en\": \"\", \"name_ru\": \"Гамбия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+224-##-###-###\", \"cc\": \"GN\", \"name_en\": \"Guinea\", \"desc_en\": \"\", \"name_ru\": \"Гвинея\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+240-##-###-####\", \"cc\": \"GQ\", \"name_en\": \"Equatorial Guinea\", \"desc_en\": \"\", \"name_ru\": \"Экваториальная Гвинея\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+30(###)###-####\", \"cc\": \"GR\", \"name_en\": \"Greece\", \"desc_en\": \"\", \"name_ru\": \"Греция\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+502-#-###-####\", \"cc\": \"GT\", \"name_en\": \"Guatemala\", \"desc_en\": \"\", \"name_ru\": \"Гватемала\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+1(671)###-####\", \"cc\": \"GU\", \"name_en\": \"Guam\", \"desc_en\": \"\", \"name_ru\": \"Гуам\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+245-#-######\", \"cc\": \"GW\", \"name_en\": \"Guinea-Bissau\", \"desc_en\": \"\", \"name_ru\": \"Гвинея-Бисау\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+592-###-####\", \"cc\": \"GY\", \"name_en\": \"Guyana\", \"desc_en\": \"\", \"name_ru\": \"Гайана\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+852-####-####\", \"cc\": \"HK\", \"name_en\": \"Hong Kong\", \"desc_en\": \"\", \"name_ru\": \"Гонконг\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+504-####-####\", \"cc\": \"HN\", \"name_en\": \"Honduras\", \"desc_en\": \"\", \"name_ru\": \"Гондурас\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+385-##-###-###\", \"cc\": \"HR\", \"name_en\": \"Croatia\", \"desc_en\": \"\", \"name_ru\": \"Хорватия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+509-##-##-####\", \"cc\": \"HT\", \"name_en\": \"Haiti\", \"desc_en\": \"\", \"name_ru\": \"Гаити\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+36(###)###-###\", \"cc\": \"HU\", \"name_en\": \"Hungary\", \"desc_en\": \"\", \"name_ru\": \"Венгрия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+62(8##)###-####\", \"cc\": \"ID\", \"name_en\": \"Indonesia \", \"desc_en\": \"mobile\", \"name_ru\": \"Индонезия \", \"desc_ru\": \"мобильные\" },\n" +
        "  { \"mask\": \"+62-##-###-##\", \"cc\": \"ID\", \"name_en\": \"Indonesia\", \"desc_en\": \"\", \"name_ru\": \"Индонезия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+62-##-###-###\", \"cc\": \"ID\", \"name_en\": \"Indonesia\", \"desc_en\": \"\", \"name_ru\": \"Индонезия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+62-##-###-####\", \"cc\": \"ID\", \"name_en\": \"Indonesia\", \"desc_en\": \"\", \"name_ru\": \"Индонезия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+62(8##)###-###\", \"cc\": \"ID\", \"name_en\": \"Indonesia \", \"desc_en\": \"mobile\", \"name_ru\": \"Индонезия \", \"desc_ru\": \"мобильные\" },\n" +
        "  { \"mask\": \"+62(8##)###-##-###\", \"cc\": \"ID\", \"name_en\": \"Indonesia \", \"desc_en\": \"mobile\", \"name_ru\": \"Индонезия \", \"desc_ru\": \"мобильные\" },\n" +
        "  { \"mask\": \"+353(###)###-###\", \"cc\": \"IE\", \"name_en\": \"Ireland\", \"desc_en\": \"\", \"name_ru\": \"Ирландия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+972-5#-###-####\", \"cc\": \"IL\", \"name_en\": \"Israel \", \"desc_en\": \"mobile\", \"name_ru\": \"Израиль \", \"desc_ru\": \"мобильные\" },\n" +
        "  { \"mask\": \"+972-#-###-####\", \"cc\": \"IL\", \"name_en\": \"Israel\", \"desc_en\": \"\", \"name_ru\": \"Израиль\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+91(####)###-###\", \"cc\": \"IN\", \"name_en\": \"India\", \"desc_en\": \"\", \"name_ru\": \"Индия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+246-###-####\", \"cc\": \"IO\", \"name_en\": \"Diego Garcia\", \"desc_en\": \"\", \"name_ru\": \"Диего-Гарсия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+964(###)###-####\", \"cc\": \"IQ\", \"name_en\": \"Iraq\", \"desc_en\": \"\", \"name_ru\": \"Ирак\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+98(###)###-####\", \"cc\": \"IR\", \"name_en\": \"Iran\", \"desc_en\": \"\", \"name_ru\": \"Иран\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+354-###-####\", \"cc\": \"IS\", \"name_en\": \"Iceland\", \"desc_en\": \"\", \"name_ru\": \"Исландия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+39(###)####-###\", \"cc\": \"IT\", \"name_en\": \"Italy\", \"desc_en\": \"\", \"name_ru\": \"Италия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+1(876)###-####\", \"cc\": \"JM\", \"name_en\": \"Jamaica\", \"desc_en\": \"\", \"name_ru\": \"Ямайка\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+962-#-####-####\", \"cc\": \"JO\", \"name_en\": \"Jordan\", \"desc_en\": \"\", \"name_ru\": \"Иордания\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+81-##-####-####\", \"cc\": \"JP\", \"name_en\": \"Japan \", \"desc_en\": \"mobile\", \"name_ru\": \"Япония \", \"desc_ru\": \"мобильные\" },\n" +
        "  { \"mask\": \"+81(###)###-###\", \"cc\": \"JP\", \"name_en\": \"Japan\", \"desc_en\": \"\", \"name_ru\": \"Япония\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+254-###-######\", \"cc\": \"KE\", \"name_en\": \"Kenya\", \"desc_en\": \"\", \"name_ru\": \"Кения\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+996(###)###-###\", \"cc\": \"KG\", \"name_en\": \"Kyrgyzstan\", \"desc_en\": \"\", \"name_ru\": \"Киргизия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+855-##-###-###\", \"cc\": \"KH\", \"name_en\": \"Cambodia\", \"desc_en\": \"\", \"name_ru\": \"Камбоджа\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+686-##-###\", \"cc\": \"KI\", \"name_en\": \"Kiribati\", \"desc_en\": \"\", \"name_ru\": \"Кирибати\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+269-##-#####\", \"cc\": \"KM\", \"name_en\": \"Comoros\", \"desc_en\": \"\", \"name_ru\": \"Коморы\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+1(869)###-####\", \"cc\": \"KN\", \"name_en\": \"Saint Kitts & Nevis\", \"desc_en\": \"\", \"name_ru\": \"Сент-Китс и Невис\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+850-191-###-####\", \"cc\": \"KP\", \"name_en\": \"DPR Korea (North) \", \"desc_en\": \"mobile\", \"name_ru\": \"Корейская НДР \", \"desc_ru\": \"мобильные\" },\n" +
        "  { \"mask\": \"+850-##-###-###\", \"cc\": \"KP\", \"name_en\": \"DPR Korea (North)\", \"desc_en\": \"\", \"name_ru\": \"Корейская НДР\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+850-###-####-###\", \"cc\": \"KP\", \"name_en\": \"DPR Korea (North)\", \"desc_en\": \"\", \"name_ru\": \"Корейская НДР\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+850-###-###\", \"cc\": \"KP\", \"name_en\": \"DPR Korea (North)\", \"desc_en\": \"\", \"name_ru\": \"Корейская НДР\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+850-####-####\", \"cc\": \"KP\", \"name_en\": \"DPR Korea (North)\", \"desc_en\": \"\", \"name_ru\": \"Корейская НДР\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+850-####-#############\", \"cc\": \"KP\", \"name_en\": \"DPR Korea (North)\", \"desc_en\": \"\", \"name_ru\": \"Корейская НДР\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+82-##-###-####\", \"cc\": \"KR\", \"name_en\": \"Korea (South)\", \"desc_en\": \"\", \"name_ru\": \"Респ. Корея\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+965-####-####\", \"cc\": \"KW\", \"name_en\": \"Kuwait\", \"desc_en\": \"\", \"name_ru\": \"Кувейт\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+1(345)###-####\", \"cc\": \"KY\", \"name_en\": \"Cayman Islands\", \"desc_en\": \"\", \"name_ru\": \"Каймановы острова\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+7(6##)###-##-##\", \"cc\": \"KZ\", \"name_en\": \"Kazakhstan\", \"desc_en\": \"\", \"name_ru\": \"Казахстан\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+7(7##)###-##-##\", \"cc\": \"KZ\", \"name_en\": \"Kazakhstan\", \"desc_en\": \"\", \"name_ru\": \"Казахстан\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+856(20##)###-###\", \"cc\": \"LA\", \"name_en\": \"Laos \", \"desc_en\": \"mobile\", \"name_ru\": \"Лаос \", \"desc_ru\": \"мобильные\" },\n" +
        "  { \"mask\": \"+856-##-###-###\", \"cc\": \"LA\", \"name_en\": \"Laos\", \"desc_en\": \"\", \"name_ru\": \"Лаос\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+961-##-###-###\", \"cc\": \"LB\", \"name_en\": \"Lebanon \", \"desc_en\": \"mobile\", \"name_ru\": \"Ливан \", \"desc_ru\": \"мобильные\" },\n" +
        "  { \"mask\": \"+961-#-###-###\", \"cc\": \"LB\", \"name_en\": \"Lebanon\", \"desc_en\": \"\", \"name_ru\": \"Ливан\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+1(758)###-####\", \"cc\": \"LC\", \"name_en\": \"Saint Lucia\", \"desc_en\": \"\", \"name_ru\": \"Сент-Люсия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+423(###)###-####\", \"cc\": \"LI\", \"name_en\": \"Liechtenstein\", \"desc_en\": \"\", \"name_ru\": \"Лихтенштейн\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+94-##-###-####\", \"cc\": \"LK\", \"name_en\": \"Sri Lanka\", \"desc_en\": \"\", \"name_ru\": \"Шри-Ланка\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+231-##-###-###\", \"cc\": \"LR\", \"name_en\": \"Liberia\", \"desc_en\": \"\", \"name_ru\": \"Либерия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+266-#-###-####\", \"cc\": \"LS\", \"name_en\": \"Lesotho\", \"desc_en\": \"\", \"name_ru\": \"Лесото\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+370(###)##-###\", \"cc\": \"LT\", \"name_en\": \"Lithuania\", \"desc_en\": \"\", \"name_ru\": \"Литва\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+352(###)###-###\", \"cc\": \"LU\", \"name_en\": \"Luxembourg\", \"desc_en\": \"\", \"name_ru\": \"Люксембург\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+371-##-###-###\", \"cc\": \"LV\", \"name_en\": \"Latvia\", \"desc_en\": \"\", \"name_ru\": \"Латвия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+218-##-###-###\", \"cc\": \"LY\", \"name_en\": \"Libya\", \"desc_en\": \"\", \"name_ru\": \"Ливия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+218-21-###-####\", \"cc\": \"LY\", \"name_en\": \"Libya\", \"desc_en\": \"Tripoli\", \"name_ru\": \"Ливия\", \"desc_ru\": \"Триполи\" },\n" +
        "  { \"mask\": \"+212-##-####-###\", \"cc\": \"MA\", \"name_en\": \"Morocco\", \"desc_en\": \"\", \"name_ru\": \"Марокко\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+377(###)###-###\", \"cc\": \"MC\", \"name_en\": \"Monaco\", \"desc_en\": \"\", \"name_ru\": \"Монако\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+377-##-###-###\", \"cc\": \"MC\", \"name_en\": \"Monaco\", \"desc_en\": \"\", \"name_ru\": \"Монако\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+373-####-####\", \"cc\": \"MD\", \"name_en\": \"Moldova\", \"desc_en\": \"\", \"name_ru\": \"Молдова\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+382-##-###-###\", \"cc\": \"ME\", \"name_en\": \"Montenegro\", \"desc_en\": \"\", \"name_ru\": \"Черногория\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+261-##-##-#####\", \"cc\": \"MG\", \"name_en\": \"Madagascar\", \"desc_en\": \"\", \"name_ru\": \"Мадагаскар\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+692-###-####\", \"cc\": \"MH\", \"name_en\": \"Marshall Islands\", \"desc_en\": \"\", \"name_ru\": \"Маршалловы Острова\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+389-##-###-###\", \"cc\": \"MK\", \"name_en\": \"Republic of Macedonia\", \"desc_en\": \"\", \"name_ru\": \"Респ. Македония\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+223-##-##-####\", \"cc\": \"ML\", \"name_en\": \"Mali\", \"desc_en\": \"\", \"name_ru\": \"Мали\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+95-##-###-###\", \"cc\": \"MM\", \"name_en\": \"Burma (Myanmar)\", \"desc_en\": \"\", \"name_ru\": \"Бирма (Мьянма)\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+95-#-###-###\", \"cc\": \"MM\", \"name_en\": \"Burma (Myanmar)\", \"desc_en\": \"\", \"name_ru\": \"Бирма (Мьянма)\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+95-###-###\", \"cc\": \"MM\", \"name_en\": \"Burma (Myanmar)\", \"desc_en\": \"\", \"name_ru\": \"Бирма (Мьянма)\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+976-##-##-####\", \"cc\": \"MN\", \"name_en\": \"Mongolia\", \"desc_en\": \"\", \"name_ru\": \"Монголия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+853-####-####\", \"cc\": \"MO\", \"name_en\": \"Macau\", \"desc_en\": \"\", \"name_ru\": \"Макао\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+1(670)###-####\", \"cc\": \"MP\", \"name_en\": \"Northern Mariana Islands\", \"desc_en\": \"\", \"name_ru\": \"Северные Марианские острова Сайпан\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+596(###)##-##-##\", \"cc\": \"MQ\", \"name_en\": \"Martinique\", \"desc_en\": \"\", \"name_ru\": \"Мартиника\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+222-##-##-####\", \"cc\": \"MR\", \"name_en\": \"Mauritania\", \"desc_en\": \"\", \"name_ru\": \"Мавритания\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+1(664)###-####\", \"cc\": \"MS\", \"name_en\": \"Montserrat\", \"desc_en\": \"\", \"name_ru\": \"Монтсеррат\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+356-####-####\", \"cc\": \"MT\", \"name_en\": \"Malta\", \"desc_en\": \"\", \"name_ru\": \"Мальта\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+230-###-####\", \"cc\": \"MU\", \"name_en\": \"Mauritius\", \"desc_en\": \"\", \"name_ru\": \"Маврикий\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+960-###-####\", \"cc\": \"MV\", \"name_en\": \"Maldives\", \"desc_en\": \"\", \"name_ru\": \"Мальдивские острова\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+265-1-###-###\", \"cc\": \"MW\", \"name_en\": \"Malawi\", \"desc_en\": \"Telecom Ltd\", \"name_ru\": \"Малави\", \"desc_ru\": \"Telecom Ltd\" },\n" +
        "  { \"mask\": \"+265-#-####-####\", \"cc\": \"MW\", \"name_en\": \"Malawi\", \"desc_en\": \"\", \"name_ru\": \"Малави\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+52(###)###-####\", \"cc\": \"MX\", \"name_en\": \"Mexico\", \"desc_en\": \"\", \"name_ru\": \"Мексика\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+52-##-##-####\", \"cc\": \"MX\", \"name_en\": \"Mexico\", \"desc_en\": \"\", \"name_ru\": \"Мексика\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+60-##-###-####\", \"cc\": \"MY\", \"name_en\": \"Malaysia \", \"desc_en\": \"mobile\", \"name_ru\": \"Малайзия \", \"desc_ru\": \"мобильные\" },\n" +
        "  { \"mask\": \"+60(###)###-###\", \"cc\": \"MY\", \"name_en\": \"Malaysia\", \"desc_en\": \"\", \"name_ru\": \"Малайзия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+60-##-###-###\", \"cc\": \"MY\", \"name_en\": \"Malaysia\", \"desc_en\": \"\", \"name_ru\": \"Малайзия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+60-#-###-###\", \"cc\": \"MY\", \"name_en\": \"Malaysia\", \"desc_en\": \"\", \"name_ru\": \"Малайзия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+258-##-###-###\", \"cc\": \"MZ\", \"name_en\": \"Mozambique\", \"desc_en\": \"\", \"name_ru\": \"Мозамбик\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+264-##-###-####\", \"cc\": \"NA\", \"name_en\": \"Namibia\", \"desc_en\": \"\", \"name_ru\": \"Намибия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+687-##-####\", \"cc\": \"NC\", \"name_en\": \"New Caledonia\", \"desc_en\": \"\", \"name_ru\": \"Новая Каледония\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+227-##-##-####\", \"cc\": \"NE\", \"name_en\": \"Niger\", \"desc_en\": \"\", \"name_ru\": \"Нигер\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+672-3##-###\", \"cc\": \"NF\", \"name_en\": \"Norfolk Island\", \"desc_en\": \"\", \"name_ru\": \"Норфолк (остров)\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+234(###)###-####\", \"cc\": \"NG\", \"name_en\": \"Nigeria\", \"desc_en\": \"\", \"name_ru\": \"Нигерия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+234-##-###-###\", \"cc\": \"NG\", \"name_en\": \"Nigeria\", \"desc_en\": \"\", \"name_ru\": \"Нигерия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+234-##-###-##\", \"cc\": \"NG\", \"name_en\": \"Nigeria\", \"desc_en\": \"\", \"name_ru\": \"Нигерия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+234(###)###-####\", \"cc\": \"NG\", \"name_en\": \"Nigeria \", \"desc_en\": \"mobile\", \"name_ru\": \"Нигерия \", \"desc_ru\": \"мобильные\" },\n" +
        "  { \"mask\": \"+505-####-####\", \"cc\": \"NI\", \"name_en\": \"Nicaragua\", \"desc_en\": \"\", \"name_ru\": \"Никарагуа\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+31-##-###-####\", \"cc\": \"NL\", \"name_en\": \"Netherlands\", \"desc_en\": \"\", \"name_ru\": \"Нидерланды\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+47(###)##-###\", \"cc\": \"NO\", \"name_en\": \"Norway\", \"desc_en\": \"\", \"name_ru\": \"Норвегия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+977-##-###-###\", \"cc\": \"NP\", \"name_en\": \"Nepal\", \"desc_en\": \"\", \"name_ru\": \"Непал\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+674-###-####\", \"cc\": \"NR\", \"name_en\": \"Nauru\", \"desc_en\": \"\", \"name_ru\": \"Науру\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+683-####\", \"cc\": \"NU\", \"name_en\": \"Niue\", \"desc_en\": \"\", \"name_ru\": \"Ниуэ\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+64(###)###-###\", \"cc\": \"NZ\", \"name_en\": \"New Zealand\", \"desc_en\": \"\", \"name_ru\": \"Новая Зеландия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+64-##-###-###\", \"cc\": \"NZ\", \"name_en\": \"New Zealand\", \"desc_en\": \"\", \"name_ru\": \"Новая Зеландия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+64(###)###-####\", \"cc\": \"NZ\", \"name_en\": \"New Zealand\", \"desc_en\": \"\", \"name_ru\": \"Новая Зеландия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+968-##-###-###\", \"cc\": \"OM\", \"name_en\": \"Oman\", \"desc_en\": \"\", \"name_ru\": \"Оман\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+507-###-####\", \"cc\": \"PA\", \"name_en\": \"Panama\", \"desc_en\": \"\", \"name_ru\": \"Панама\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+51(###)###-###\", \"cc\": \"PE\", \"name_en\": \"Peru\", \"desc_en\": \"\", \"name_ru\": \"Перу\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+689-##-##-##\", \"cc\": \"PF\", \"name_en\": \"French Polynesia\", \"desc_en\": \"\", \"name_ru\": \"Французская Полинезия (Таити)\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+675(###)##-###\", \"cc\": \"PG\", \"name_en\": \"Papua New Guinea\", \"desc_en\": \"\", \"name_ru\": \"Папуа-Новая Гвинея\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+63(###)###-####\", \"cc\": \"PH\", \"name_en\": \"Philippines\", \"desc_en\": \"\", \"name_ru\": \"Филиппины\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+92(###)###-####\", \"cc\": \"PK\", \"name_en\": \"Pakistan\", \"desc_en\": \"\", \"name_ru\": \"Пакистан\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+48(###)###-###\", \"cc\": \"PL\", \"name_en\": \"Poland\", \"desc_en\": \"\", \"name_ru\": \"Польша\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+970-##-###-####\", \"cc\": \"PS\", \"name_en\": \"Palestine\", \"desc_en\": \"\", \"name_ru\": \"Палестина\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+351-##-###-####\", \"cc\": \"PT\", \"name_en\": \"Portugal\", \"desc_en\": \"\", \"name_ru\": \"Португалия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+680-###-####\", \"cc\": \"PW\", \"name_en\": \"Palau\", \"desc_en\": \"\", \"name_ru\": \"Палау\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+595(###)###-###\", \"cc\": \"PY\", \"name_en\": \"Paraguay\", \"desc_en\": \"\", \"name_ru\": \"Парагвай\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+974-####-####\", \"cc\": \"QA\", \"name_en\": \"Qatar\", \"desc_en\": \"\", \"name_ru\": \"Катар\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+262-#####-####\", \"cc\": \"RE\", \"name_en\": \"Reunion\", \"desc_en\": \"\", \"name_ru\": \"Реюньон\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+40-##-###-####\", \"cc\": \"RO\", \"name_en\": \"Romania\", \"desc_en\": \"\", \"name_ru\": \"Румыния\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+381-##-###-####\", \"cc\": \"RS\", \"name_en\": \"Serbia\", \"desc_en\": \"\", \"name_ru\": \"Сербия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+7(###)###-##-##\", \"cc\": \"RU\", \"name_en\": \"Russia\", \"desc_en\": \"\", \"name_ru\": \"Россия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+250(###)###-###\", \"cc\": \"RW\", \"name_en\": \"Rwanda\", \"desc_en\": \"\", \"name_ru\": \"Руанда\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+966-5-####-####\", \"cc\": \"SA\", \"name_en\": \"Saudi Arabia \", \"desc_en\": \"mobile\", \"name_ru\": \"Саудовская Аравия \", \"desc_ru\": \"мобильные\" },\n" +
        "  { \"mask\": \"+966-#-###-####\", \"cc\": \"SA\", \"name_en\": \"Saudi Arabia\", \"desc_en\": \"\", \"name_ru\": \"Саудовская Аравия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+677-###-####\", \"cc\": \"SB\", \"name_en\": \"Solomon Islands \", \"desc_en\": \"mobile\", \"name_ru\": \"Соломоновы Острова \", \"desc_ru\": \"мобильные\" },\n" +
        "  { \"mask\": \"+677-#####\", \"cc\": \"SB\", \"name_en\": \"Solomon Islands\", \"desc_en\": \"\", \"name_ru\": \"Соломоновы Острова\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+248-#-###-###\", \"cc\": \"SC\", \"name_en\": \"Seychelles\", \"desc_en\": \"\", \"name_ru\": \"Сейшелы\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+249-##-###-####\", \"cc\": \"SD\", \"name_en\": \"Sudan\", \"desc_en\": \"\", \"name_ru\": \"Судан\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+46-##-###-####\", \"cc\": \"SE\", \"name_en\": \"Sweden\", \"desc_en\": \"\", \"name_ru\": \"Швеция\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+65-####-####\", \"cc\": \"SG\", \"name_en\": \"Singapore\", \"desc_en\": \"\", \"name_ru\": \"Сингапур\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+290-####\", \"cc\": \"SH\", \"name_en\": \"Saint Helena\", \"desc_en\": \"\", \"name_ru\": \"Остров Святой Елены\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+290-####\", \"cc\": \"SH\", \"name_en\": \"Tristan da Cunha\", \"desc_en\": \"\", \"name_ru\": \"Тристан-да-Кунья\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+386-##-###-###\", \"cc\": \"SI\", \"name_en\": \"Slovenia\", \"desc_en\": \"\", \"name_ru\": \"Словения\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+421(###)###-###\", \"cc\": \"SK\", \"name_en\": \"Slovakia\", \"desc_en\": \"\", \"name_ru\": \"Словакия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+232-##-######\", \"cc\": \"SL\", \"name_en\": \"Sierra Leone\", \"desc_en\": \"\", \"name_ru\": \"Сьерра-Леоне\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+378-####-######\", \"cc\": \"SM\", \"name_en\": \"San Marino\", \"desc_en\": \"\", \"name_ru\": \"Сан-Марино\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+221-##-###-####\", \"cc\": \"SN\", \"name_en\": \"Senegal\", \"desc_en\": \"\", \"name_ru\": \"Сенегал\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+252-##-###-###\", \"cc\": \"SO\", \"name_en\": \"Somalia\", \"desc_en\": \"\", \"name_ru\": \"Сомали\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+252-#-###-###\", \"cc\": \"SO\", \"name_en\": \"Somalia\", \"desc_en\": \"\", \"name_ru\": \"Сомали\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+252-#-###-###\", \"cc\": \"SO\", \"name_en\": \"Somalia \", \"desc_en\": \"mobile\", \"name_ru\": \"Сомали \", \"desc_ru\": \"мобильные\" },\n" +
        "  { \"mask\": \"+597-###-####\", \"cc\": \"SR\", \"name_en\": \"Suriname \", \"desc_en\": \"mobile\", \"name_ru\": \"Суринам \", \"desc_ru\": \"мобильные\" },\n" +
        "  { \"mask\": \"+597-###-###\", \"cc\": \"SR\", \"name_en\": \"Suriname\", \"desc_en\": \"\", \"name_ru\": \"Суринам\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+211-##-###-####\", \"cc\": \"SS\", \"name_en\": \"South Sudan\", \"desc_en\": \"\", \"name_ru\": \"Южный Судан\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+239-##-#####\", \"cc\": \"ST\", \"name_en\": \"Sao Tome and Principe\", \"desc_en\": \"\", \"name_ru\": \"Сан-Томе и Принсипи\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+503-##-##-####\", \"cc\": \"SV\", \"name_en\": \"El Salvador\", \"desc_en\": \"\", \"name_ru\": \"Сальвадор\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+1(721)###-####\", \"cc\": \"SX\", \"name_en\": \"Sint Maarten\", \"desc_en\": \"\", \"name_ru\": \"Синт-Маартен\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+963-##-####-###\", \"cc\": \"SY\", \"name_en\": \"Syrian Arab Republic\", \"desc_en\": \"\", \"name_ru\": \"Сирийская арабская республика\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+268-##-##-####\", \"cc\": \"SZ\", \"name_en\": \"Swaziland\", \"desc_en\": \"\", \"name_ru\": \"Свазиленд\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+1(649)###-####\", \"cc\": \"TC\", \"name_en\": \"Turks & Caicos\", \"desc_en\": \"\", \"name_ru\": \"Тёркс и Кайкос\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+235-##-##-##-##\", \"cc\": \"TD\", \"name_en\": \"Chad\", \"desc_en\": \"\", \"name_ru\": \"Чад\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+228-##-###-###\", \"cc\": \"TG\", \"name_en\": \"Togo\", \"desc_en\": \"\", \"name_ru\": \"Того\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+66-##-###-####\", \"cc\": \"TH\", \"name_en\": \"Thailand \", \"desc_en\": \"mobile\", \"name_ru\": \"Таиланд \", \"desc_ru\": \"мобильные\" },\n" +
        "  { \"mask\": \"+66-##-###-###\", \"cc\": \"TH\", \"name_en\": \"Thailand\", \"desc_en\": \"\", \"name_ru\": \"Таиланд\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+992-##-###-####\", \"cc\": \"TJ\", \"name_en\": \"Tajikistan\", \"desc_en\": \"\", \"name_ru\": \"Таджикистан\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+690-####\", \"cc\": \"TK\", \"name_en\": \"Tokelau\", \"desc_en\": \"\", \"name_ru\": \"Токелау\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+670-###-####\", \"cc\": \"TL\", \"name_en\": \"East Timor\", \"desc_en\": \"\", \"name_ru\": \"Восточный Тимор\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+670-77#-#####\", \"cc\": \"TL\", \"name_en\": \"East Timor\", \"desc_en\": \"Timor Telecom\", \"name_ru\": \"Восточный Тимор\", \"desc_ru\": \"Timor Telecom\" },\n" +
        "  { \"mask\": \"+670-78#-#####\", \"cc\": \"TL\", \"name_en\": \"East Timor\", \"desc_en\": \"Timor Telecom\", \"name_ru\": \"Восточный Тимор\", \"desc_ru\": \"Timor Telecom\" },\n" +
        "  { \"mask\": \"+993-#-###-####\", \"cc\": \"TM\", \"name_en\": \"Turkmenistan\", \"desc_en\": \"\", \"name_ru\": \"Туркменистан\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+216-##-###-###\", \"cc\": \"TN\", \"name_en\": \"Tunisia\", \"desc_en\": \"\", \"name_ru\": \"Тунис\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+676-#####\", \"cc\": \"TO\", \"name_en\": \"Tonga\", \"desc_en\": \"\", \"name_ru\": \"Тонга\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+90(###)###-####\", \"cc\": \"TR\", \"name_en\": \"Turkey\", \"desc_en\": \"\", \"name_ru\": \"Турция\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+1(868)###-####\", \"cc\": \"TT\", \"name_en\": \"Trinidad & Tobago\", \"desc_en\": \"\", \"name_ru\": \"Тринидад и Тобаго\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+688-90####\", \"cc\": \"TV\", \"name_en\": \"Tuvalu \", \"desc_en\": \"mobile\", \"name_ru\": \"Тувалу \", \"desc_ru\": \"мобильные\" },\n" +
        "  { \"mask\": \"+688-2####\", \"cc\": \"TV\", \"name_en\": \"Tuvalu\", \"desc_en\": \"\", \"name_ru\": \"Тувалу\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+886-#-####-####\", \"cc\": \"TW\", \"name_en\": \"Taiwan\", \"desc_en\": \"\", \"name_ru\": \"Тайвань\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+886-####-####\", \"cc\": \"TW\", \"name_en\": \"Taiwan\", \"desc_en\": \"\", \"name_ru\": \"Тайвань\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+255-##-###-####\", \"cc\": \"TZ\", \"name_en\": \"Tanzania\", \"desc_en\": \"\", \"name_ru\": \"Танзания\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+380(##)###-##-##\", \"cc\": \"UA\", \"name_en\": \"Ukraine\", \"desc_en\": \"\", \"name_ru\": \"Украина\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+256(###)###-###\", \"cc\": \"UG\", \"name_en\": \"Uganda\", \"desc_en\": \"\", \"name_ru\": \"Уганда\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+44-##-####-####\", \"cc\": \"UK\", \"name_en\": \"United Kingdom\", \"desc_en\": \"\", \"name_ru\": \"Великобритания\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+598-#-###-##-##\", \"cc\": \"UY\", \"name_en\": \"Uruguay\", \"desc_en\": \"\", \"name_ru\": \"Уругвай\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+998-##-###-####\", \"cc\": \"UZ\", \"name_en\": \"Uzbekistan\", \"desc_en\": \"\", \"name_ru\": \"Узбекистан\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+39-6-698-#####\", \"cc\": \"VA\", \"name_en\": \"Vatican City\", \"desc_en\": \"\", \"name_ru\": \"Ватикан\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+1(784)###-####\", \"cc\": \"VC\", \"name_en\": \"Saint Vincent & the Grenadines\", \"desc_en\": \"\", \"name_ru\": \"Сент-Винсент и Гренадины\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+58(###)###-####\", \"cc\": \"VE\", \"name_en\": \"Venezuela\", \"desc_en\": \"\", \"name_ru\": \"Венесуэла\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+1(284)###-####\", \"cc\": \"VG\", \"name_en\": \"British Virgin Islands\", \"desc_en\": \"\", \"name_ru\": \"Британские Виргинские острова\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+1(340)###-####\", \"cc\": \"VI\", \"name_en\": \"US Virgin Islands\", \"desc_en\": \"\", \"name_ru\": \"Американские Виргинские острова\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+84-##-####-###\", \"cc\": \"VN\", \"name_en\": \"Vietnam\", \"desc_en\": \"\", \"name_ru\": \"Вьетнам\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+84(###)####-###\", \"cc\": \"VN\", \"name_en\": \"Vietnam\", \"desc_en\": \"\", \"name_ru\": \"Вьетнам\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+678-##-#####\", \"cc\": \"VU\", \"name_en\": \"Vanuatu \", \"desc_en\": \"mobile\", \"name_ru\": \"Вануату \", \"desc_ru\": \"мобильные\" },\n" +
        "  { \"mask\": \"+678-#####\", \"cc\": \"VU\", \"name_en\": \"Vanuatu\", \"desc_en\": \"\", \"name_ru\": \"Вануату\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+681-##-####\", \"cc\": \"WF\", \"name_en\": \"Wallis and Futuna\", \"desc_en\": \"\", \"name_ru\": \"Уоллис и Футуна\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+685-##-####\", \"cc\": \"WS\", \"name_en\": \"Samoa\", \"desc_en\": \"\", \"name_ru\": \"Самоа\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+967-###-###-###\", \"cc\": \"YE\", \"name_en\": \"Yemen \", \"desc_en\": \"mobile\", \"name_ru\": \"Йемен \", \"desc_ru\": \"мобильные\" },\n" +
        "  { \"mask\": \"+967-#-###-###\", \"cc\": \"YE\", \"name_en\": \"Yemen\", \"desc_en\": \"\", \"name_ru\": \"Йемен\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+967-##-###-###\", \"cc\": \"YE\", \"name_en\": \"Yemen\", \"desc_en\": \"\", \"name_ru\": \"Йемен\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+27-##-###-####\", \"cc\": \"ZA\", \"name_en\": \"South Africa\", \"desc_en\": \"\", \"name_ru\": \"Южно-Африканская Респ.\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+260-##-###-####\", \"cc\": \"ZM\", \"name_en\": \"Zambia\", \"desc_en\": \"\", \"name_ru\": \"Замбия\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+263-#-######\", \"cc\": \"ZW\", \"name_en\": \"Zimbabwe\", \"desc_en\": \"\", \"name_ru\": \"Зимбабве\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+1(###)###-####\", \"cc\": \"CA\", \"name_en\": \"Canada\", \"desc_en\": \"\", \"name_ru\": \"Канада\", \"desc_ru\": \"\" },\n" +
        "  { \"mask\": \"+1(###)###-####\", \"cc\": \"US\", \"name_en\": \"USA\", \"desc_en\": \"\", \"name_ru\": \"США\", \"desc_ru\": \"\" }\n" +
        "]\n"





fun EditText.setupPhoneMask(
    countryListener: (CountryFlagModel?) -> Unit = {},
    validListener: (Boolean) -> Unit = {}
): FormatWatcher {
    var currentCountry: CountryFlagModel? = null
    val inputMask = MaskImpl.createNonTerminated(PhoneNumberUnderscoreSlotsParser().parseSlots("+_"))
    val formatWatcher = MaskFormatWatcher(inputMask)
    formatWatcher.installOn(this)
    var tempOldValue: String? = null
    val phoneTranslater = context.getPhoneCodeTranslater()
    formatWatcher.setCallback(object : FormattedTextChangeListener {
        override fun beforeFormatting(oldValue: String?, newValue: String?): Boolean {

            tempOldValue = oldValue?.replace(Regex("[+()-]"), "")
            val number = newValue?.replace(Regex("[+()-]"), "") ?: ""
            if (number.isEmpty()) {
                inputMask.clear()
                formatWatcher.swapMask(inputMask)
                currentCountry = null
                countryListener(currentCountry)
            } else {
                val country = phoneTranslater.getPhoneMask(number)
                if (country != null) {
                    if (country != currentCountry) {
                        currentCountry = country
                        val inputMask =
                            MaskImpl.createTerminated(PhoneNumberUnderscoreSlotsParser().parseSlots(country.freeMask))
                        inputMask.insertFront(number)
                        formatWatcher.swapMask(inputMask)

                        countryListener(currentCountry)
                    }
                } else {
                    if (tempOldValue.isNullOrEmpty() || currentCountry == null) {
                        inputMask.clear()
                        formatWatcher.swapMask(inputMask)
                        currentCountry = null
                        countryListener(currentCountry)
                    } else {
                        val inputMask =
                            MaskImpl.createTerminated(PhoneNumberUnderscoreSlotsParser().parseSlots(currentCountry!!.freeMask))
                        inputMask.insertFront(tempOldValue)
                        formatWatcher.swapMask(inputMask)
                    }
                }
            }
            return false
        }

        override fun onTextFormatted(formatter: FormatWatcher?, newFormattedText: String?) {
            validListener.invoke(
                currentCountry != null && newFormattedText != null && mathPhoneByMask(
                    newFormattedText,
                    currentCountry?.mask
                )
            )
        } })
    return formatWatcher
}


private fun mathPhoneByMask(phone: String?, mask: String?): Boolean {

    if (mask == null || phone == null) {
        return false
    }


    if (mask.length != phone.length) {
        return false
    }



    for (i in 0 until mask.length) {

        if (mask[i] != phone[i] && (mask[i] != '#' && phone[i].isDigit())) {
            return false
        }
    }

    return true
}


fun Context.getPhoneCodeTranslater(): CountryDecisionTree {

    val gson = GsonBuilder().create()
    val countryFlagModels =
        gson.fromJson<List<CountryFlagModel>>(text, object : TypeToken<List<CountryFlagModel>>() {}.type)
    for (i in countryFlagModels) {
        i.freeMask = i.mask.replace(Regex("[0-9#]"), "_")
        i.cipher = i.mask.replace(Regex("[+()-]"), "").replace('#', '.')
    }
    val tree = CountryDecisionTree()
    tree.fillDecisionTree(countryFlagModels)
    return tree
}

class CountryDecisionTree {
    val root = Node()

    fun fillDecisionTree(models: List<CountryFlagModel>) {
        for (i in models) {
            addPhoneMask(i)
        }
    }

    fun addPhoneMask(country: CountryFlagModel) {
        val phoneMaskCode = "${country.cipher}!"
        var curNode = root
        for (digit in phoneMaskCode) {
            if (digit != '!') { // if char isn't an end of mask code
                if (curNode.next.contains(digit)) {
                    curNode = curNode.next[digit]!! // if next node already exists move to it and continue
                    continue
                } else {
                    val newNode = Node()
                    curNode.next[digit] = newNode // if node not exists - create and move to it and continue
                    curNode = newNode
                    continue
                }
            } else {
                curNode.country = country // when code ends write down country
            }
        }
    }

    fun getPhoneMask(phone: String): CountryFlagModel? {
        if (phone == "") return null
        val possibleCandidates = Stack<Pair<Int, Node>>()
        var curNode = root
        var i = 0
        while (i < phone.length) {
            val digit = phone[i]
            if (curNode.next.contains(digit)) {
                if (curNode.next.contains('.')) // if node with this digit exists but also exists general mask
                    possibleCandidates.push(Pair(i, curNode.next['.']!!))
                curNode = curNode.next[digit]!! // if node with this digit exists move to it
                i++
                continue
            } else if (curNode.next.contains('.')) {
                curNode =
                    curNode.next['.']!! // if node with this digits doesn't exists but some mask has free space on that place
                i++
                continue
            } else { // seems phone is not valid
                if (possibleCandidates.size != 0) {
                    val possibleCandidate = possibleCandidates.pop()
                    i = possibleCandidate.first
                    curNode = possibleCandidate.second
                    i++
                    continue
                } else {
                    return null
                }
            }
        }
        while (curNode.country == null) { // if curNode is not last find most common least number mask
            if (curNode.next.contains('.')) { // going down the most common way
                curNode =
                    curNode.next['.']!! // if node with this digits doesn't exists but some mask has free space on that place
                continue
            } else {
                curNode =
                    curNode.next.valueAt(0) // must not throw because  if node have no country it has to have at least one node next
                continue
            }
        }
        return curNode.country
    }


    class Node {
        var country: CountryFlagModel? = null
        var next: androidx.collection.ArrayMap<Char, Node> = androidx.collection.ArrayMap()
    }
}

class CountryFlagModel(
    val mask: String,
    val cc: String,
    val name_en: String,
    val desc_en: String,
    val name_ru: String,
    val desc_ru: String,
    var freeMask: String,
    var cipher: String
)