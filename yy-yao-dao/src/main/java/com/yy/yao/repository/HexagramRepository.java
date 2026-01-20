package com.yy.yao.repository;

import com.yy.yao.model.Hexagram;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * 64卦数据仓库
 */
@Repository
public class HexagramRepository {

    private final Map<String, Hexagram> hexagramsByBinary;
    private final Map<Integer, Hexagram> hexagramsByNumber;

    public HexagramRepository() {
        this.hexagramsByBinary = new HashMap<>();
        this.hexagramsByNumber = new HashMap<>();
        initializeHexagrams();
    }

    /**
     * 根据二进制表示获取卦象
     */
    public Optional<Hexagram> findByBinary(String binary) {
        return Optional.ofNullable(hexagramsByBinary.get(binary));
    }

    /**
     * 根据卦序号获取卦象
     */
    public Optional<Hexagram> findByNumber(int number) {
        return Optional.ofNullable(hexagramsByNumber.get(number));
    }

    /**
     * 获取所有卦象
     */
    public List<Hexagram> findAll() {
        return new ArrayList<>(hexagramsByNumber.values());
    }

    /**
     * 初始化64卦数据
     */
    private void initializeHexagrams() {
        // 这里实现部分核心卦象, 实际应包含全部64卦
        addHexagram(createHexagram(1, "乾", "☰☰", "111111", "乾", "乾",
                "乾: 元亨,利贞。",
                "乾卦象征天,代表刚健、进取。元亨利贞,万事亨通,利于正道。",
                "天、创造、刚健、阳气、领导力、积极进取",
                "此时正是发奋进取的好时机，但需警惕过度自信。",
                Arrays.asList(
                        new Hexagram.LineStatement(1, "初九", "潜龙勿用。", "时机未到,应当潜藏待时。"),
                        new Hexagram.LineStatement(2, "九二", "见龙在田,利见大人。", "时机显现,宜求贤达之助。"),
                        new Hexagram.LineStatement(3, "九三", "君子终日乾乾,夕惕若厉,无咎。", "当勤勉不懈,警惕危险。"),
                        new Hexagram.LineStatement(4, "九四", "或跃在渊,无咎。", "可以跃起,也可退守,无咎。"),
                        new Hexagram.LineStatement(5, "九五", "飞龙在天,利见大人。", "位高权重,利于展现才能。"),
                        new Hexagram.LineStatement(6, "上九", "亢龙有悔。", "过于刚强,应当知进退。")
                )));

        addHexagram(createHexagram(2, "坤", "☷☷", "000000", "坤", "坤",
                "坤: 元亨,利牝马之贞。君子有攸往,先迷后得主,利西南得朋,东北丧朋。安贞吉。",
                "坤卦象征地,代表柔顺、包容。当顺应天时,厚德载物。",
                "地、承载、柔顺、包容、被动、稳定",
                "此时宜采取被动、等待的态度，不可急进。",
                Arrays.asList(
                        new Hexagram.LineStatement(1, "初六", "履霜,坚冰至。", "见微知著,防患未然。"),
                        new Hexagram.LineStatement(2, "六二", "直方大,不习无不利。", "正直宽广,自然无不利。"),
                        new Hexagram.LineStatement(3, "六三", "含章可贞,或从王事,无成有终。", "含蓄美德,辅佐他人。"),
                        new Hexagram.LineStatement(4, "六四", "括囊,无咎无誉。", "谨慎收敛,可保无咎。"),
                        new Hexagram.LineStatement(5, "六五", "黄裳,元吉。", "中正柔顺,大吉之象。"),
                        new Hexagram.LineStatement(6, "上六", "龙战于野,其血玄黄。", "阴极阳生,物极必反。")
                )));

        addHexagram(createHexagram(3, "屯", "☵☳", "010001", "坎", "震",
                "屯: 元亨利贞。勿用有攸往,利建侯。",
                "屯卦象征万物初生,艰难困顿。当守正道,不宜轻举妄动。",
                createDefaultLines()));

        addHexagram(createHexagram(11, "泰", "☷☰", "000111", "坤", "乾",
                "泰: 小往大来,吉亨。",
                "泰卦象征通泰,天地交泰。阴阳和合,万事亨通。",
                createDefaultLines()));

        addHexagram(createHexagram(12, "否", "☰☷", "111000", "乾", "坤",
                "否: 否之匪人,不利君子贞,大往小来。",
                "否卦象征闭塞,天地不交。当守正待时,不可妄动。",
                createDefaultLines()));

        addHexagram(createHexagram(23, "剥", "☶☷", "100000", "艮", "坤",
                "剥: 不利有攸往。",
                "剥卦象征剥落,阴盛阳衰。当顺势而为,静待时机。",
                createDefaultLines()));

        addHexagram(createHexagram(24, "复", "☷☳", "000001", "坤", "震",
                "复: 亨。出入无疾,朋来无咎。反复其道,七日来复,利有攸往。",
                "复卦象征复返,一阳来复。阳气初生,万象更新。",
                createDefaultLines()));

        addHexagram(createHexagram(63, "既济", "☵☲", "010101", "坎", "离",
                "既济: 亨小,利贞,初吉终乱。",
                "既济卦象征成功完成,但需警惕,盛极而衰。当慎终如始。",
                createDefaultLines()));

        addHexagram(createHexagram(64, "未济", "☲☵", "101010", "离", "坎",
                "未济: 亨,小狐汔济,濡其尾,无攸利。",
                "未济卦象征尚未完成,事业未竟。当继续努力,不可懈怠。",
                createDefaultLines()));

        // 补充其他重要卦象
        fillRemainingHexagrams();
    }

    private Hexagram createHexagram(int number, String name, String symbol, String binary,
                                     String upperTrigram, String lowerTrigram,
                                     String statement, String interpretation,
                                     List<Hexagram.LineStatement> lineStatements) {
        return new Hexagram(number, name, symbol, binary, upperTrigram, lowerTrigram,
                statement, interpretation, null, null, lineStatements);
    }

    /**
     * 创建卦象（带symbolism和advice）
     */
    private Hexagram createHexagram(int number, String name, String symbol, String binary,
                                     String upperTrigram, String lowerTrigram,
                                     String statement, String interpretation,
                                     String symbolism, String advice,
                                     List<Hexagram.LineStatement> lineStatements) {
        return new Hexagram(number, name, symbol, binary, upperTrigram, lowerTrigram,
                statement, interpretation, symbolism, advice, lineStatements);
    }

    private void addHexagram(Hexagram hexagram) {
        hexagramsByBinary.put(hexagram.getBinaryCode(), hexagram);
        hexagramsByNumber.put(hexagram.getNumber(), hexagram);
    }

    private List<Hexagram.LineStatement> createDefaultLines() {
        return Arrays.asList(
                new Hexagram.LineStatement(1, "初爻", "初爻爻辞", "初爻解释"),
                new Hexagram.LineStatement(2, "二爻", "二爻爻辞", "二爻解释"),
                new Hexagram.LineStatement(3, "三爻", "三爻爻辞", "三爻解释"),
                new Hexagram.LineStatement(4, "四爻", "四爻爻辞", "四爻解释"),
                new Hexagram.LineStatement(5, "五爻", "五爻爻辞", "五爻解释"),
                new Hexagram.LineStatement(6, "上爻", "上爻爻辞", "上爻解释")
        );
    }

    /**
     * 补充剩余卦象的基本数据
     */
    private void fillRemainingHexagrams() {
        String[][] basicHexagrams = {
                {"4", "蒙", "001010", "艮", "坎", "蒙: 亨。匪我求童蒙,童蒙求我。", "蒙卦象征启蒙教育。"},
                {"5", "需", "010111", "坎", "乾", "需: 有孚,光亨,贞吉。利涉大川。", "需卦象征等待时机。"},
                {"6", "讼", "111010", "乾", "坎", "讼: 有孚,窒惕,中吉,终凶。利见大人,不利涉大川。", "讼卦象征争讼。"},
                {"7", "师", "000010", "坤", "坎", "师: 贞,丈人吉,无咎。", "师卦象征军队。"},
                {"8", "比", "010000", "坎", "坤", "比: 吉。原筮,元永贞,无咎。", "比卦象征亲比。"},
                {"9", "小畜", "110111", "巽", "乾", "小畜: 亨。密云不雨,自我西郊。", "小畜卦象征小有积蓄。"},
                {"10", "履", "111011", "乾", "兑", "履: 履虎尾,不咥人,亨。", "履卦象征实践。"},
                {"13", "同人", "111101", "乾", "离", "同人: 同人于野,亨。利涉大川,利君子贞。", "同人卦象征团结。"},
                {"14", "大有", "101111", "离", "乾", "大有: 元亨。", "大有卦象征大有收获。"},
                {"15", "谦", "000100", "坤", "艮", "谦: 亨,君子有终。", "谦卦象征谦逊。"},
                {"16", "豫", "001000", "震", "坤", "豫: 利建侯行师。", "豫卦象征喜悦。"},
                {"17", "随", "011001", "兑", "震", "随: 元亨利贞,无咎。", "随卦象征跟随。"},
                {"18", "蛊", "100110", "艮", "巽", "蛊: 元亨,利涉大川。", "蛊卦象征整治。"},
                {"19", "临", "000011", "坤", "兑", "临: 元亨利贞。至于八月有凶。", "临卦象征临近。"},
                {"20", "观", "110000", "巽", "坤", "观: 盥而不荐,有孚颙若。", "观卦象征观察。"},
                {"21", "噬嗑", "101001", "离", "震", "噬嗑: 亨,利用狱。", "噬嗑卦象征咬合。"},
                {"22", "贲", "100101", "艮", "离", "贲: 亨,小利有攸往。", "贲卦象征装饰。"},
                {"25", "无妄", "111001", "乾", "震", "无妄: 元亨利贞。其匪正有眚,不利有攸往。", "无妄卦象征无妄。"},
                {"26", "大畜", "100111", "艮", "乾", "大畜: 利贞,不家食吉,利涉大川。", "大畜卦象征大有积蓄。"},
                {"27", "颐", "100001", "艮", "震", "颐: 贞吉。观颐,自求口实。", "颐卦象征养育。"},
                {"28", "大过", "011110", "兑", "巽", "大过: 栋桡。利有攸往,亨。", "大过卦象征大有过度。"},
                {"29", "坎", "010010", "坎", "坎", "坎: 习坎,有孚,维心亨,行有尚。", "坎卦象征险难。"},
                {"30", "离", "101101", "离", "离", "离: 利贞,亨。畜牝牛,吉。", "离卦象征光明。"},
                {"31", "咸", "011100", "兑", "艮", "咸: 亨,利贞,取女吉。", "咸卦象征感应。"},
                {"32", "恒", "001110", "震", "巽", "恒: 亨,无咎,利贞,利有攸往。", "恒卦象征恒久。"},
                {"33", "遯", "111100", "乾", "艮", "遯: 亨,小利贞。", "遯卦象征退避。"},
                {"34", "大壮", "001111", "震", "乾", "大壮: 利贞。", "大壮卦象征强壮。"},
                {"35", "晋", "101000", "离", "坤", "晋: 康侯用锡马蕃庶,昼日三接。", "晋卦象征前进。"},
                {"36", "明夷", "000101", "坤", "离", "明夷: 利艰贞。", "明夷卦象征光明受伤。"},
                {"37", "家人", "110101", "巽", "离", "家人: 利女贞。", "家人卦象征家庭。"},
                {"38", "睽", "101011", "离", "兑", "睽: 小事吉。", "睽卦象征乖离。"},
                {"39", "蹇", "010100", "坎", "艮", "蹇: 利西南,不利东北。利见大人,贞吉。", "蹇卦象征艰难。"},
                {"40", "解", "001010", "震", "坎", "解: 利西南,无所往,其来复吉。有攸往,夙吉。", "解卦象征解除。"},
                {"41", "损", "100011", "艮", "兑", "损: 有孚,元吉,无咎,可贞,利有攸往。", "损卦象征减损。"},
                {"42", "益", "110001", "巽", "震", "益: 利有攸往,利涉大川。", "益卦象征增益。"},
                {"43", "夬", "011111", "兑", "乾", "夬: 扬于王庭,孚号有厉。告自邑,不利即戎,利有攸往。", "夬卦象征决断。"},
                {"44", "姤", "111110", "乾", "巽", "姤: 女壮,勿用取女。", "姤卦象征相遇。"},
                {"45", "萃", "011000", "兑", "坤", "萃: 亨,王假有庙。利见大人,亨,利贞。用大牲吉,利有攸往。", "萃卦象征聚集。"},
                {"46", "升", "000110", "坤", "巽", "升: 元亨,用见大人,勿恤。南征吉。", "升卦象征上升。"},
                {"47", "困", "011010", "兑", "坎", "困: 亨,贞,大人吉,无咎。有言不信。", "困卦象征困顿。"},
                {"48", "井", "010110", "坎", "巽", "井: 改邑不改井,无丧无得。往来井井。", "井卦象征水井。"},
                {"49", "革", "011101", "兑", "离", "革: 巳日乃孚,元亨利贞,悔亡。", "革卦象征变革。"},
                {"50", "鼎", "101110", "离", "巽", "鼎: 元吉,亨。", "鼎卦象征鼎器。"},
                {"51", "震", "001001", "震", "震", "震: 亨。震来虩虩,笑言哑哑。", "震卦象征雷动。"},
                {"52", "艮", "100100", "艮", "艮", "艮: 艮其背,不获其身,行其庭,不见其人,无咎。", "艮卦象征止止。"},
                {"53", "渐", "110100", "巽", "艮", "渐: 女归吉,利贞。", "渐卦象征渐进。"},
                {"54", "归妹", "001011", "震", "兑", "归妹: 征凶,无攸利。", "归妹卦象征嫁女。"},
                {"55", "丰", "001101", "震", "离", "丰: 亨,王假之。勿忧,宜日中。", "丰卦象征丰盛。"},
                {"56", "旅", "101100", "离", "艮", "旅: 小亨,旅贞吉。", "旅卦象征旅行。"},
                {"57", "巽", "110110", "巽", "巽", "巽: 小亨,利有攸往,利见大人。", "巽卦象征风。"},
                {"58", "兑", "011011", "兑", "兑", "兑: 亨,利贞。", "兑卦象征喜悦。"},
                {"59", "涣", "110010", "巽", "坎", "涣: 亨,王假有庙。利涉大川,利贞。", "涣卦象征涣散。"},
                {"60", "节", "010011", "坎", "兑", "节: 亨,苦节不可贞。", "节卦象征节制。"},
                {"61", "中孚", "110011", "巽", "兑", "中孚: 豚鱼吉,利涉大川,利贞。", "中孚卦象征诚信。"},
                {"62", "小过", "001100", "震", "艮", "小过: 亨,利贞。可小事,不可大事。", "小过卦象征小有过度。"}
        };

        for (String[] data : basicHexagrams) {
            if (!hexagramsByNumber.containsKey(Integer.parseInt(data[0]))) {
                addHexagram(createHexagram(
                        Integer.parseInt(data[0]),
                        data[1],
                        "",
                        data[2],
                        data[3],
                        data[4],
                        data[5],
                        data[6],
                        createDefaultLines()
                ));
            }
        }
    }
}
