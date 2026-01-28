
-- ============================================
-- 3. 爻辞表数据
-- ============================================
TRUNCATE TABLE hexagram_lines;

-- 第1卦 乾
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (1, 1, '初九', 'yang', '潜龙勿用。龙德而隐者也。不易乎世，不成乎名，遁世无闷，不见是而无闷。乐则行之，忧则违之。确乎其不可拔，潜龙也。'),
                                                                              (1, 2, '九二', 'yang', '见龙在田，利见大人。德施普也。'),
                                                                              (1, 3, '九三', 'yang', '君子终日乾乾，夕惕若，厉无咎。反复之，次第之，强不息也。'),
                                                                              (1, 4, '九四', 'yang', '或跃在渊，无咎。进无咎也。'),
                                                                              (1, 5, '九五', 'yang', '飞龙在天，利见大人。飞之为言亦为言也。'),
                                                                              (1, 6, '上九', 'yang', '亢龙有悔。贵而无位，高而无民，贤人在下位而无辅，是以动而有悔也。');

-- 第2卦 坤
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (2, 1, '初六', 'yin', '履霜，坚冰至。阴始凝也，驯致其道。至坚冰，履虽厉，无咎也。'),
                                                                              (2, 2, '六二', 'yin', '直方大，不习无不利。地道也。'),
                                                                              (2, 3, '六三', 'yin', '含章可贞。或从王事，无成有终。'),
                                                                              (2, 4, '六四', 'yin', '括囊，无咎无誉。慎不害也。'),
                                                                              (2, 5, '六五', 'yin', '黄裳元吉。文在中也。'),
                                                                              (2, 6, '上六', 'yin', '龙战于野，其血玄黄。阴疑于阳，必战，为其嫌于无阳也。');

-- 第3卦 屯
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (3, 1, '初九', 'yang', '磐桓，利居贞，利建侯。磐桓即盘桓，回旋不进之意。'),
                                                                              (3, 2, '六二', 'yin', '屯如邅如，乘马班如。匪寇，婚媾。女子贞不字，十年乃字。'),
                                                                              (3, 3, '六三', 'yin', '即鹿无虞，唯入于林中，君子几不如舍，往吝。'),
                                                                              (3, 4, '六四', 'yin', '乘马班如，求婚媾，往吉无不利。'),
                                                                              (3, 5, '九五', 'yang', '屯其膏。小贞吉，大贞凶。'),
                                                                              (3, 6, '上六', 'yin', '乘马班如，泣血涟如。');

-- 第4卦 蒙
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (4, 1, '初六', 'yin', '发蒙，利用刑人，用说桎梏，以往吝。'),
                                                                              (4, 2, '九二', 'yang', '包蒙吉。纳妇吉。子克家。'),
                                                                              (4, 3, '六三', 'yin', '勿用取女，见金夫，不有躬，无攸利。'),
                                                                              (4, 4, '六四', 'yin', '困蒙，吝。'),
                                                                              (4, 5, '六五', 'yin', '童蒙吉。'),
                                                                              (4, 6, '上九', 'yang', '击蒙，不利为寇，利御寇。');

-- 第5卦 需
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (5, 1, '初九', 'yang', '需于郊，利用恒，无咎。'),
                                                                              (5, 2, '九二', 'yang', '需于沙，小有言，终吉。'),
                                                                              (5, 3, '九三', 'yang', '需于泥，致寇至。'),
                                                                              (5, 4, '六四', 'yin', '需于血，出自穴。'),
                                                                              (5, 5, '九五', 'yang', '需于酒食，贞吉。'),
                                                                              (5, 6, '上六', 'yin', '入于穴，有不速之客三人来，敬之终吉。');

-- 第6卦 讼
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (6, 1, '初六', 'yin', '不永所事，小有言，终吉。'),
                                                                              (6, 2, '九二', 'yang', '不克讼，归而逋，其邑人三百户，无眚。'),
                                                                              (6, 3, '六三', 'yin', '食旧德，贞厉，终吉。或从王事，无成。'),
                                                                              (6, 4, '九四', 'yang', '不克讼，复即命渝，安贞吉。'),
                                                                              (6, 5, '九五', 'yang', '讼元吉。'),
                                                                              (6, 6, '上九', 'yang', '或锡之鞶带，终朝三褫之。');

-- 第7卦 师
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (7, 1, '初六', 'yin', '师出以律，否则邪。'),
                                                                              (7, 2, '九二', 'yang', '在师中，吉无咎，王三锡命。'),
                                                                              (7, 3, '六三', 'yin', '师或舆尸，凶。'),
                                                                              (7, 4, '六四', 'yin', '师左次，无咎。'),
                                                                              (7, 5, '六五', 'yin', '田有禽，利言不利行。'),
                                                                              (7, 6, '上六', 'yin', '大君有命，开国承家，小人勿用。');

-- 第8卦 比
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (8, 1, '初六', 'yin', '有孚比之，无咎。有孚盈缶，终来有它吉。'),
                                                                              (8, 2, '六二', 'yin', '比之自内，贞吉。'),
                                                                              (8, 3, '六三', 'yin', '比之匪人。'),
                                                                              (8, 4, '六四', 'yin', '外比之，贞吉。'),
                                                                              (8, 5, '九五', 'yang', '显比，王用三驱，失前禽，邑人不诫，吉。'),
                                                                              (8, 6, '上六', 'yin', '比之无首，凶。');

-- 第9卦 小畜
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (9, 1, '初九', 'yang', '复自道，何其咎，吉。'),
                                                                              (9, 2, '九二', 'yang', '牵复，吉。'),
                                                                              (9, 3, '九三', 'yang', '舆说辐，夫妻反目。'),
                                                                              (9, 4, '六四', 'yin', '有孚，血去惕出，无咎。'),
                                                                              (9, 5, '九五', 'yang', '有孚挛如，富以其邻。'),
                                                                              (9, 6, '上九', 'yang', '既雨既处，尚德载，妇贞厉。月几望，君子征凶。');

-- 第10卦 履
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (10, 1, '初九', 'yang', '素履，往无咎。'),
                                                                              (10, 2, '九二', 'yang', '履道坦坦，幽人贞吉。'),
                                                                              (10, 3, '六三', 'yin', '眇能视，跛能履，履虎尾，咥人凶。武人为大君。'),
                                                                              (10, 4, '九四', 'yang', '履虎尾，愬愬，终吉。'),
                                                                              (10, 5, '九五', 'yang', '夬履，贞厉。'),
                                                                              (10, 6, '上九', 'yang', '视履考祥，其旋元吉。');

-- 第11卦 泰
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (11, 1, '初九', 'yang', '拔茅茹，以其汇，征吉。'),
                                                                              (11, 2, '九二', 'yang', '包荒，用冯河，不遐遗，朋亡，得尚于中行。'),
                                                                              (11, 3, '九三', 'yang', '无平不陂，无往不复，艰贞无咎。勿恤其孚，于食有福。'),
                                                                              (11, 4, '六四', 'yin', '翩翩不富，以其邻，不戒以孚。'),
                                                                              (11, 5, '六五', 'yin', '帝乙归妹，以祉元吉。'),
                                                                              (11, 6, '上六', 'yin', '城复于隅，勿用师，自邑告命，贞吝。');

-- 第12卦 否
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (12, 1, '初六', 'yin', '拔茅茹，以其汇，贞吉亨。'),
                                                                              (12, 2, '六二', 'yin', '包承，小人吉，大人否亨。'),
                                                                              (12, 3, '六三', 'yin', '包羞。'),
                                                                              (12, 4, '九四', 'yang', '有命无咎，畴离祉。'),
                                                                              (12, 5, '九五', 'yang', '休否，大人吉。其亡其亡，系于苞桑。'),
                                                                              (12, 6, '上九', 'yang', '倾否，先否后喜。');

-- 第13卦 同人
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (13, 1, '初九', 'yang', '同人于门，无咎。'),
                                                                              (13, 2, '六二', 'yin', '同人于宗，吝。'),
                                                                              (13, 3, '九三', 'yang', '伏戈于莽，升其高陵，三岁不兴。'),
                                                                              (13, 4, '九四', 'yang', '乘其墉，弗克攻，吉。'),
                                                                              (13, 5, '九五', 'yang', '同人，先号啕而后笑。大师克相遇。'),
                                                                              (13, 6, '上九', 'yang', '同人于郊，无悔。');

-- 第14卦 大有
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (14, 1, '初九', 'yang', '无交害，匪咎，艰则无咎。'),
                                                                              (14, 2, '九二', 'yang', '大车以载，有攸往，无咎。'),
                                                                              (14, 3, '九三', 'yang', '公用亨于天子，小人弗克。'),
                                                                              (14, 4, '九四', 'yang', '匪其彭，无咎。'),
                                                                              (14, 5, '六五', 'yin', '厥孚交如，威如，吉。'),
                                                                              (14, 6, '上九', 'yang', '自天佑之，吉无不利。');

-- 第15卦 谦
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (15, 1, '初六', 'yin', '谦谦君子，用涉大川，吉。'),
                                                                              (15, 2, '六二', 'yin', '鸣谦，贞吉。'),
                                                                              (15, 3, '九三', 'yang', '劳谦，君子有终，吉。'),
                                                                              (15, 4, '六四', 'yin', '无不利，撝谦。'),
                                                                              (15, 5, '六五', 'yin', '不富以其邻，利用侵伐，无不利。'),
                                                                              (15, 6, '上六', 'yin', '鸣谦，利用行师，征邑国。');

-- 第16卦 豫
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (16, 1, '初六', 'yin', '鸣豫，凶。'),
                                                                              (16, 2, '六二', 'yin', '介于石，不终日，贞吉。'),
                                                                              (16, 3, '六三', 'yin', '盱豫，悔，迟有悔。'),
                                                                              (16, 4, '九四', 'yang', '由豫，大有得，勿疑，朋盍簇。'),
                                                                              (16, 5, '六五', 'yin', '贞疾，恒不死。'),
                                                                              (16, 6, '上六', 'yin', '冥豫，成有渐，无咎。');

-- 第17卦 随
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (17, 1, '初九', 'yang', '官有渝，贞吉。出门交有功。'),
                                                                              (17, 2, '六二', 'yin', '孚于人，贞吉。'),
                                                                              (17, 3, '六三', 'yin', '孚于人，亦孚于言，行而有功，言成而信，眇能视，跛能履，履虎尾，咥人凶。武人为大君。'),
                                                                              (17, 4, '九四', 'yang', '随有获，贞凶。有孚在道，以明，何咎。'),
                                                                              (17, 5, '九五', 'yang', '孚于嘉，吉。'),
                                                                              (17, 6, '上六', 'yin', '拘而系之，三岁不兴。');

-- 第18卦 蛊
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (18, 1, '初六', 'yin', '干父之蛊，有子，考无咎，厉终吉。'),
                                                                              (18, 2, '九二', 'yang', '干母之蛊，不可贞。'),
                                                                              (18, 3, '九三', 'yang', '干父之蛊，小有悔，无大咎。'),
                                                                              (18, 4, '六四', 'yin', '裕父之蛊，往见吝。'),
                                                                              (18, 5, '六五', 'yin', '干父之蛊，用誉。'),
                                                                              (18, 6, '上九', 'yang', '不事王侯，高尚其事。');

-- 第19卦 临
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (19, 1, '初九', 'yang', '咸临，贞吉。'),
                                                                              (19, 2, '九二', 'yang', '咸临，吉无不利。'),
                                                                              (19, 3, '六三', 'yin', '甘临，无攸利。既忧之，无咎。'),
                                                                              (19, 4, '六四', 'yin', '至临，无咎。'),
                                                                              (19, 5, '六五', 'yin', '知临，大君之宜，吉。'),
                                                                              (19, 6, '上六', 'yin', '敦临，吉无咎。');

-- 第20卦 观
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (20, 1, '初六', 'yin', '童观，小人无咎，君子吝。'),
                                                                              (20, 2, '六二', 'yin', '窥观，利女贞。'),
                                                                              (20, 3, '六三', 'yin', '观我生，进退。'),
                                                                              (20, 4, '六四', 'yin', '观国之光，利用宾于王。'),
                                                                              (20, 5, '九五', 'yang', '观我生，君子无咎。'),
                                                                              (20, 6, '上九', 'yang', '观其生，君子无咎。');

-- 第21卦 噬嗑
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (21, 1, '初九', 'yang', '屦校灭趾，无咎。'),
                                                                              (21, 2, '六二', 'yin', '噬肤，灭鼻，无咎。'),
                                                                              (21, 3, '六三', 'yin', '噬腊肉，小有悔。'),
                                                                              (21, 4, '九四', 'yang', '噬干胏，得金矢，利艰贞，吉。'),
                                                                              (21, 5, '六五', 'yin', '噬干肉，得黄金，贞厉，无咎。'),
                                                                              (21, 6, '上九', 'yang', '何校灭耳，凶。');

-- 第22卦 贲
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (22, 1, '初九', 'yang', '贲其趾，舍车而徒。'),
                                                                              (22, 2, '六二', 'yin', '贲其须。'),
                                                                              (22, 3, '九三', 'yang', '贲如，濡如，永贞吉。'),
                                                                              (22, 4, '六四', 'yin', '贲如皤如，白马翰如，匪寇婚媾。'),
                                                                              (22, 5, '六五', 'yin', '贲于丘园，束帛戋戋，吝，终吉。'),
                                                                              (22, 6, '上九', 'yang', '白贲，无咎。');

-- 第23卦 剥
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (23, 1, '初六', 'yin', '剥床以足，蔑贞凶。'),
                                                                              (23, 2, '六二', 'yin', '剥床以辨，凶。'),
                                                                              (23, 3, '六三', 'yin', '剥之，无咎。'),
                                                                              (23, 4, '六四', 'yin', '剥床以肤，凶。'),
                                                                              (23, 5, '六五', 'yin', '剥床以毛，凶。'),
                                                                              (23, 6, '上九', 'yang', '君子得舆，小人剥庐。');

-- 第24卦 复
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (24, 1, '初九', 'yang', '不远复，无只悔，元吉。'),
                                                                              (24, 2, '六二', 'yin', '休复，吉。'),
                                                                              (24, 3, '六三', 'yin', '频复，厉无咎。'),
                                                                              (24, 4, '六四', 'yin', '中行独复。'),
                                                                              (24, 5, '六五', 'yin', '敦复，无悔。'),
                                                                              (24, 6, '上六', 'yin', '迷复，凶，有灾眚，用行师，终有大败，以其国君，凶，至于十年不克征。');

-- 第25卦 无妄
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (25, 1, '初九', 'yang', '无妄，往吉。'),
                                                                              (25, 2, '六二', 'yin', '不耕获，不菑畲，则利有攸往。'),
                                                                              (25, 3, '六三', 'yin', '无妄之灾，或系之牛，行人之得，邑人之灾。'),
                                                                              (25, 4, '九四', 'yang', '可贞，无咎。'),
                                                                              (25, 5, '九五', 'yang', '无妄之疾，勿药有喜。'),
                                                                              (25, 6, '上九', 'yang', '无妄，行有眚，无攸利。');

-- 第26卦 大畜
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (26, 1, '初九', 'yang', '有厉，利已。'),
                                                                              (26, 2, '九二', 'yang', '舆说辐。'),
                                                                              (26, 3, '九三', 'yang', '良马逐，利艰贞，曰闲舆卫，利有攸往。'),
                                                                              (26, 4, '六四', 'yin', '童牛之牿，元吉。'),
                                                                              (26, 5, '六五', 'yin', '豶豕之牙，吉。'),
                                                                              (26, 6, '上九', 'yang', '何天之衢，亨。');

-- 第27卦 颐
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (27, 1, '初九', 'yang', '舍尔灵龟，观我丰，凶。'),
                                                                              (27, 2, '六二', 'yin', '颠颐，拂经，于丘颐，征凶。'),
                                                                              (27, 3, '六三', 'yin', '拂颐，贞厉，十年勿用，无攸利。'),
                                                                              (27, 4, '六四', 'yin', '颠颐，吉。虎视眈眈，其欲逐逐，无咎。'),
                                                                              (27, 5, '六五', 'yin', '拂经，居贞吉，不可涉大川。'),
                                                                              (27, 6, '上九', 'yang', '由颐，厉吉，利涉大川。');

-- 第28卦 大过
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (28, 1, '初六', 'yin', '藉用白茅，无咎。'),
                                                                              (28, 2, '九二', 'yang', '枯杨生稊，老夫得其女妻，无不利。'),
                                                                              (28, 3, '九三', 'yang', '栋桡，凶。'),
                                                                              (28, 4, '九四', 'yang', '栋隆，吉，有它吝。'),
                                                                              (28, 5, '九五', 'yang', '枯杨生华，老妇得士夫，无咎无誉。'),
                                                                              (28, 6, '上六', 'yin', '过涉灭顶，凶，无咎。');

-- 第29卦 坎
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (29, 1, '初六', 'yin', '习坎，入于坎窞，凶。'),
                                                                              (29, 2, '九二', 'yang', '坎有险，求小得。'),
                                                                              (29, 3, '六三', 'yin', '来之坎坎，险且枕，入于坎窞，勿用。'),
                                                                              (29, 4, '六四', 'yin', '樽酒，簋贰，用缶，纳约自牖，终无咎。'),
                                                                              (29, 5, '九五', 'yang', '坎不盈，祇既平，无咎。'),
                                                                              (29, 6, '上六', 'yin', '用徽纆，置于丛棘，三岁不得，凶。');

-- 第30卦 离
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (30, 1, '初九', 'yang', '履错然，敬之无咎。'),
                                                                              (30, 2, '六二', 'yin', '黄离，元吉。'),
                                                                              (30, 3, '九三', 'yang', '日昃之离，不鼓缶而歌，则大耋之嗟，凶。'),
                                                                              (30, 4, '九四', 'yang', '突如其来如，焚如，死如，弃如。'),
                                                                              (30, 5, '六五', 'yin', '出涕沱若，戚嗟若，吉。'),
                                                                              (30, 6, '上九', 'yang', '王用出征，有嘉折首，获匪其丑，无咎。');

-- 第31卦 咸
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (31, 1, '初六', 'yin', '咸其拇。'),
                                                                              (31, 2, '六二', 'yin', '咸其腓，凶，居之吝。'),
                                                                              (31, 3, '九三', 'yang', '咸其股，执其随，往吝。'),
                                                                              (31, 4, '九四', 'yang', '贞吉悔亡，憧憧往来，朋从尔思。'),
                                                                              (31, 5, '九五', 'yang', '咸其脢，无悔。'),
                                                                              (31, 6, '上六', 'yin', '咸其辅颊舌。');

-- 第32卦 恒
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (32, 1, '初六', 'yin', '浚恒，贞凶，无攸利。'),
                                                                              (32, 2, '九二', 'yang', '悔亡。'),
                                                                              (32, 3, '九三', 'yang', '不恒其德，或承之羞，贞吝。'),
                                                                              (32, 4, '九四', 'yang', '田无禽。'),
                                                                              (32, 5, '六五', 'yin', '恒其德，贞，妇人吉，夫子凶。'),
                                                                              (32, 6, '上六', 'yin', '浚恒，凶。');

-- 第33卦 遁
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (33, 1, '初六', 'yin', '遁尾，厉，勿用有攸往。'),
                                                                              (33, 2, '六二', 'yin', '执之用黄牛之革，莫之胜说。'),
                                                                              (33, 3, '九三', 'yang', '系遁，有疾厉，畜臣妾吉。'),
                                                                              (33, 4, '九四', 'yang', '好遁，君子吉，小人否。'),
                                                                              (33, 5, '九五', 'yang', '嘉遁，贞吉。'),
                                                                              (33, 6, '上九', 'yang', '肥遁，无不利。');

-- 第34卦 大壮
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (34, 1, '初九', 'yang', '壮于趾，征凶，有孚。'),
                                                                              (34, 2, '九二', 'yang', '贞吉。'),
                                                                              (34, 3, '九三', 'yang', '小人用壮，君子用罔，贞厉。羝羊触藩，赢其角。'),
                                                                              (34, 4, '九四', 'yang', '贞吉悔亡，藩决不羸，壮于大舆之輹。'),
                                                                              (34, 5, '六五', 'yin', '丧羊于易，无悔。'),
                                                                              (34, 6, '上六', 'yin', '羝羊触藩，不能退，不能遂，无攸利，艰则吉。');

-- 第35卦 晋
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (35, 1, '初六', 'yin', '晋如，摧如，贞吉。罔孚，裕无咎。'),
                                                                              (35, 2, '六二', 'yin', '晋如，愁如，贞吉。受兹介福，于其王母。'),
                                                                              (35, 3, '六三', 'yin', '众允，悔亡。'),
                                                                              (35, 4, '九四', 'yang', '晋如，鼫鼠，贞厉。'),
                                                                              (35, 5, '六五', 'yin', '悔亡，失得勿恤，往吉无不利。'),
                                                                              (35, 6, '上九', 'yang', '晋其角，维用伐邑，厉吉无咎，贞吝。');

-- 第36卦 明夷
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (36, 1, '初九', 'yang', '明夷于飞，垂其翼。君子于行，三日不食，有攸往，主人有言。'),
                                                                              (36, 2, '六二', 'yin', '明夷，夷于左股，用拯马壮吉。'),
                                                                              (36, 3, '九三', 'yang', '明夷于南狩，得其大首，不可疾贞。'),
                                                                              (36, 4, '六四', 'yin', '入于左腹，获明夷之心，于出门庭。'),
                                                                              (36, 5, '六五', 'yin', '箕子之明夷，利贞。'),
                                                                              (36, 6, '上六', 'yin', '不明晦，初登于天，后入于地。');

-- 第37卦 家人
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (37, 1, '初九', 'yang', '闲有家，悔亡。'),
                                                                              (37, 2, '六二', 'yin', '无攸遂，在中馈，贞吉。'),
                                                                              (37, 3, '九三', 'yang', '家人嗣嗣，悔厉吉。妇子嘻嘻，终吝。'),
                                                                              (37, 4, '六四', 'yin', '富家，大吉。'),
                                                                              (37, 5, '九五', 'yang', '王假之，家人肃肃，悔亡。'),
                                                                              (37, 6, '上九', 'yang', '有孚威如，终吉。');

-- 第38卦 睽
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (38, 1, '初九', 'yang', '悔亡，丧马勿逐，自复。见恶人无咎。'),
                                                                              (38, 2, '九二', 'yang', '遇主于巷，无咎。'),
                                                                              (38, 3, '九三', 'yang', '见舆曳，其牛掣，其人天且劓，无初有终。'),
                                                                              (38, 4, '九四', 'yang', '睽孤，遇元夫，交孚，厉无咎。'),
                                                                              (38, 5, '六五', 'yin', '悔亡，厥宗噬肤，往何咎。'),
                                                                              (38, 6, '上九', 'yang', '睽孤，见豕负涂，载鬼一车，先张之弧，后说之弧，匪寇婚媾，往遇雨则吉。');

-- 第39卦 蹇
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (39, 1, '初六', 'yin', '往蹇，来誉。'),
                                                                              (39, 2, '六二', 'yin', '王臣蹇蹇，匪躬之故。'),
                                                                              (39, 3, '九三', 'yang', '往蹇，来反。'),
                                                                              (39, 4, '六四', 'yin', '往蹇，来连。'),
                                                                              (39, 5, '九五', 'yang', '大蹇，朋来。'),
                                                                              (39, 6, '上六', 'yin', '往蹇，来硕，吉。利见大人。');

-- 第40卦 解
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (40, 1, '初六', 'yin', '无咎。'),
                                                                              (40, 2, '九二', 'yang', '田获三狐，得黄金，贞吉。'),
                                                                              (40, 3, '六三', 'yin', '负且乘，致寇至，贞吝。'),
                                                                              (40, 4, '九四', 'yang', '解而拇，朋至斯孚。'),
                                                                              (40, 5, '六五', 'yin', '君子维有解，吉。有孚于小人。'),
                                                                              (40, 6, '上六', 'yin', '公用射隼，于高墉之上，获之无不利。');

-- 第41卦 损
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (41, 1, '初九', 'yang', '已事遄往，无咎，酌损之。'),
                                                                              (41, 2, '九二', 'yang', '利贞，征凶，弗损益之。'),
                                                                              (41, 3, '六三', 'yin', '三人行，则损一人。一人行，则得其友。'),
                                                                              (41, 4, '六四', 'yin', '损其疾，使遄有喜，无咎。'),
                                                                              (41, 5, '六五', 'yin', '或益之十朋之龟，弗克违，元吉。'),
                                                                              (41, 6, '上九', 'yang', '莫益之，或击之，立心勿恒，凶。');

-- 第42卦 益
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (42, 1, '初九', 'yang', '利用为大作，元吉，无咎。'),
                                                                              (42, 2, '六二', 'yin', '或益之十朋之龟，弗克违，永贞吉。王用亨于帝，吉。'),
                                                                              (42, 3, '六三', 'yin', '益之用凶事，无咎。有孚中行，告公用圭。'),
                                                                              (42, 4, '六四', 'yin', '中行告公，从，利用为依迁国。'),
                                                                              (42, 5, '九五', 'yang', '有孚惠心，勿问元吉。有孚惠我德。'),
                                                                              (42, 6, '上九', 'yang', '莫益之，或击之，立心勿恒，凶。');

-- 第43卦 夬
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (43, 1, '初九', 'yang', '壮于前趾，往不胜，为咎。'),
                                                                              (43, 2, '九二', 'yang', '惕号，莫夜有戎，勿恤。'),
                                                                              (43, 3, '九三', 'yang', '壮于頄，有凶。君子夬夬，独行，遇雨若濡，有厉无咎。'),
                                                                              (43, 4, '九四', 'yang', '臀无肤，其行次且。牵羊悔亡，闻言不信。'),
                                                                              (43, 5, '九五', 'yang', '苋陆夬夬，中行无咎。'),
                                                                              (43, 6, '上六', 'yin', '无号，终有凶。');

-- 第44卦 姤
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (44, 1, '初六', 'yin', '系于金柅，贞吉，有攸往，见凶，羸豕孚蹢躅。'),
                                                                              (44, 2, '九二', 'yang', '包有鱼，无咎，不利宾。'),
                                                                              (44, 3, '九三', 'yang', '臀无肤，其行次且，厉，无大咎。'),
                                                                              (44, 4, '九四', 'yang', '包无鱼，起凶。'),
                                                                              (44, 5, '九五', 'yang', '以杞包瓜，含章，有陨自天。'),
                                                                              (44, 6, '上九', 'yang', '姤其角，吝，无咎。');

-- 第45卦 萃
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (45, 1, '初六', 'yin', '有孚不终，乃乱乃萃，若号一呼而后笑，勿恤，往无咎。'),
                                                                              (45, 2, '六二', 'yin', '引吉，无咎，孚乃利用禴。'),
                                                                              (45, 3, '六三', 'yin', '萃如，嗟如，无咎，往无咎，小吝。'),
                                                                              (45, 4, '九四', 'yang', '大吉，无咎。'),
                                                                              (45, 5, '九五', 'yang', '萃有位，无咎。匪孚，元永贞，悔亡。'),
                                                                              (45, 6, '上六', 'yin', '赍咨涕洟，无咎。');

-- 第46卦 升
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (46, 1, '初六', 'yin', '允升，大吉。'),
                                                                              (46, 2, '九二', 'yang', '孚乃利用禴，无咎。'),
                                                                              (46, 3, '九三', 'yang', '升虚邑。'),
                                                                              (46, 4, '六四', 'yin', '王用亨于岐山，吉无咎。'),
                                                                              (46, 5, '六五', 'yin', '贞吉，升阶。'),
                                                                              (46, 6, '上六', 'yin', '冥升，利于不息之贞。');

-- 第47卦 困
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (47, 1, '初六', 'yin', '臀困于株木，入于幽谷，三岁不觉。'),
                                                                              (47, 2, '九二', 'yang', '困于酒食，朱绂方来，利用享祀，征凶，无咎。'),
                                                                              (47, 3, '六三', 'yin', '困于石，据于蒺藜，入于其宫，不见其妻，凶。'),
                                                                              (47, 4, '九四', 'yang', '来徐徐，困于金车，吝，终吉。'),
                                                                              (47, 5, '九五', 'yang', '劓刖，困于赤绂，乃徐有说，利用祭祀。'),
                                                                              (47, 6, '上六', 'yin', '困于葛藟，于臲卼，曰动悔，有悔，征吉。');

-- 第48卦 井
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (48, 1, '初六', 'yin', '井泥不食，旧井无禽。'),
                                                                              (48, 2, '九二', 'yang', '井谷射鲋，瓮敝漏。'),
                                                                              (48, 3, '九三', 'yang', '井渫不食，为我心恻，可用汲，王明，并受其福。'),
                                                                              (48, 4, '六四', 'yin', '井甃，无咎。'),
                                                                              (48, 5, '九五', 'yang', '井冽，寒泉食。'),
                                                                              (48, 6, '上六', 'yin', '井收勿幕，有孚元吉。');

-- 第49卦 革
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (49, 1, '初九', 'yang', '巩用黄牛之革，莫之胜说。'),
                                                                              (49, 2, '六二', 'yin', '己日乃革之，征吉，无咎。'),
                                                                              (49, 3, '九三', 'yang', '征凶，贞厉，革言三就，有孚。'),
                                                                              (49, 4, '九四', 'yang', '悔亡，有孚，改命吉。'),
                                                                              (49, 5, '九五', 'yang', '大人虎变，未占有孚。'),
                                                                              (49, 6, '上六', 'yin', '君子豹变，小人革面，征凶，居贞吉。');

-- 第50卦 鼎
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (50, 1, '初六', 'yin', '鼎颠趾，利出否，得妾以其子，无咎。'),
                                                                              (50, 2, '九二', 'yang', '鼎有实，我仇有疾，不我能即，吉。'),
                                                                              (50, 3, '九三', 'yang', '鼎耳革，其行塞，雉膏不食，方雨亏悔，终吉。'),
                                                                              (50, 4, '九四', 'yang', '鼎折足，覆公餗，其形渥，凶。'),
                                                                              (50, 5, '六五', 'yin', '鼎黄耳，金铉，利贞。'),
                                                                              (50, 6, '上九', 'yang', '鼎玉铉，大吉，无不利。');

-- 第51卦 震
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (51, 1, '初九', 'yang', '震来虩虩，后笑言哑哑，吉。'),
                                                                              (51, 2, '六二', 'yin', '震来厉，亿丧贝，跻于九陵勿逐，七日得。'),
                                                                              (51, 3, '六三', 'yin', '震苏苏，震行无眚。'),
                                                                              (51, 4, '九四', 'yang', '震遂泥。'),
                                                                              (51, 5, '六五', 'yin', '震往来厉，亿无丧，有事。'),
                                                                              (51, 6, '上六', 'yin', '震索索，视矍矍，征凶。震不于其躬，于其邻，无咎。婚媾有言。');

-- 第52卦 艮
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (52, 1, '初六', 'yin', '艮其趾，无咎，利永贞。'),
                                                                              (52, 2, '六二', 'yin', '艮其腓，不拯其随，其心不快。'),
                                                                              (52, 3, '九三', 'yang', '艮其限，列其夤，厉薰心。'),
                                                                              (52, 4, '六四', 'yin', '艮其身，无咎。'),
                                                                              (52, 5, '六五', 'yin', '艮其辅，言有序，悔亡。'),
                                                                              (52, 6, '上九', 'yang', '敦艮，吉。');

-- 第53卦 渐
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (53, 1, '初六', 'yin', '鸿渐于干，小子厉，有言，无咎。'),
                                                                              (53, 2, '六二', 'yin', '鸿渐于磐，饮食衎衎，吉。'),
                                                                              (53, 3, '九三', 'yang', '鸿渐于陆，夫征不复，妇孕不育，凶。利御寇。'),
                                                                              (53, 4, '六四', 'yin', '鸿渐于木，或得其桷，无咎。'),
                                                                              (53, 5, '九五', 'yang', '鸿渐于陵，妇三岁不孕，终莫之胜，吉。'),
                                                                              (53, 6, '上九', 'yang', '鸿渐于陆，其羽可用为仪，吉。');

-- 第54卦 归妹
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (54, 1, '初九', 'yang', '归妹以娣，跛能履，征吉。'),
                                                                              (54, 2, '九二', 'yang', '眇能视，利幽人之贞。'),
                                                                              (54, 3, '六三', 'yin', '归妹以须，反归以娣。'),
                                                                              (54, 4, '九四', 'yang', '归妹愆期，迟归有时。'),
                                                                              (54, 5, '六五', 'yin', '帝乙归妹，其君之袂不如其娣之袂良，月几望，吉。'),
                                                                              (54, 6, '上六', 'yin', '女承筐无实，士刲羊无血，无攸利。');

-- 第55卦 丰
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (55, 1, '初九', 'yang', '遇其配主，虽旬无咎，往有尚。'),
                                                                              (55, 2, '六二', 'yin', '丰其部，日中见斗，往得疑疾，有孚发若，吉。'),
                                                                              (55, 3, '九三', 'yang', '丰其沛，日中见沫，折其右肱，无咎。'),
                                                                              (55, 4, '九四', 'yang', '丰其蔀，日中见斗，遇其夷主，吉。'),
                                                                              (55, 5, '六五', 'yin', '来章有庆誉，吉。'),
                                                                              (55, 6, '上六', 'yin', '丰其屋，蔀其家，窥其户，阒其无人，三岁不觉，凶。');

-- 第56卦 旅
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (56, 1, '初六', 'yin', '旅琐琐，斯其所取灾。'),
                                                                              (56, 2, '六二', 'yin', '旅即次，怀其资，得童仆贞。'),
                                                                              (56, 3, '九三', 'yang', '旅焚其次，丧其童仆，贞厉。'),
                                                                              (56, 4, '九四', 'yang', '旅于处，得其资斧，我心不快。'),
                                                                              (56, 5, '六五', 'yin', '射雉一矢亡，终以誉命。'),
                                                                              (56, 6, '上九', 'yang', '鸟焚其巢，旅人先笑后号啕。丧牛于易，凶。');

-- 第57卦 巽
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (57, 1, '初六', 'yin', '进退，利武人之贞。'),
                                                                              (57, 2, '九二', 'yang', '巽在床下，用史巫纷若，吉无咎。'),
                                                                              (57, 3, '九三', 'yang', '频巽，吝。'),
                                                                              (57, 4, '六四', 'yin', '悔亡，田获三品。'),
                                                                              (57, 5, '九五', 'yang', '贞吉，悔亡，无不利。无初有终，先庚三日，后庚三日，吉。'),
                                                                              (57, 6, '上九', 'yang', '巽在床下，丧其资斧，贞凶。');

-- 第58卦 兑
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (58, 1, '初九', 'yang', '和兑，吉。'),
                                                                              (58, 2, '九二', 'yang', '孚兑，吉，悔亡。'),
                                                                              (58, 3, '六三', 'yin', '来兑，凶。'),
                                                                              (58, 4, '九四', 'yang', '商兑未宁，介疾有喜。'),
                                                                              (58, 5, '九五', 'yang', '孚于剥，有厉。'),
                                                                              (58, 6, '上六', 'yin', '引兑。');

-- 第59卦 涣
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (59, 1, '初六', 'yin', '用拯马壮，吉。'),
                                                                              (59, 2, '九二', 'yang', '涣奔其机，悔亡。'),
                                                                              (59, 3, '六三', 'yin', '涣其躬，无悔。'),
                                                                              (59, 4, '六四', 'yin', '涣其群，元吉。涣有丘，匪夷所思。'),
                                                                              (59, 5, '九五', 'yang', '涣汗其大号，涣王居，无咎。'),
                                                                              (59, 6, '上九', 'yang', '涣其血，去逖出，无咎。');

-- 第60卦 节
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (60, 1, '初九', 'yang', '不出户庭，无咎。'),
                                                                              (60, 2, '九二', 'yang', '不出门庭，凶。'),
                                                                              (60, 3, '六三', 'yin', '不节若，则嗟若，无咎。'),
                                                                              (60, 4, '六四', 'yin', '安节，亨。'),
                                                                              (60, 5, '九五', 'yang', '甘节，吉，往有尚。'),
                                                                              (60, 6, '上六', 'yin', '苦节，贞凶，悔亡。');

-- 第61卦 中孚
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (61, 1, '初九', 'yang', '虞吉，有它不燕。'),
                                                                              (61, 2, '九二', 'yang', '鹤鸣在阴，其子和之，我有好爵，吾与尔靡之。'),
                                                                              (61, 3, '六三', 'yin', '得敌，或鼓或罢，或泣或歌。'),
                                                                              (61, 4, '六四', 'yin', '月几望，马匹亡，无咎。'),
                                                                              (61, 5, '九五', 'yang', '有孚挛如，无咎。'),
                                                                              (61, 6, '上九', 'yang', '翰音登于天，贞凶。');

-- 第62卦 小过
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (62, 1, '初六', 'yin', '飞鸟以凶。'),
                                                                              (62, 2, '六二', 'yin', '过其祖，遇其妣，不及其君，遇其臣，无咎。'),
                                                                              (62, 3, '九三', 'yang', '弗过防之，从或戕之，凶。'),
                                                                              (62, 4, '九四', 'yang', '无咎，弗过遇之，往厉必戒，勿用永贞。'),
                                                                              (62, 5, '六五', 'yin', '密云不雨，自我西郊，公弋取彼在穴。'),
                                                                              (62, 6, '上六', 'yin', '弗遇过之，飞鸟离之，凶，是谓灾眚。');

-- 第63卦 既济
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (63, 1, '初九', 'yang', '曳其轮，濡其尾，无咎。'),
                                                                              (63, 2, '六二', 'yin', '妇丧其茀，勿逐，七日得。'),
                                                                              (63, 3, '九三', 'yang', '高宗伐鬼方，三年克之，小人勿用。'),
                                                                              (63, 4, '六四', 'yin', '繻有衣袽，终日戒。'),
                                                                              (63, 5, '九五', 'yang', '东邻杀牛，不如西邻之禴祭，实受其福。'),
                                                                              (63, 6, '上六', 'yin', '濡其首，厉。');

-- 第64卦 未济
INSERT INTO hexagram_lines (hexagram_id, position, name, type, line_text) VALUES
                                                                              (64, 1, '初六', 'yin', '濡其尾，吝。'),
                                                                              (64, 2, '九二', 'yang', '曳其轮，贞吉。'),
                                                                              (64, 3, '六三', 'yin', '未济，征凶，利涉大川。'),
                                                                              (64, 4, '九四', 'yang', '贞吉，悔亡，震用伐鬼方，三年有赏于大国。'),
                                                                              (64, 5, '六五', 'yin', '贞吉，无悔，君子之光，有孚，吉。'),
                                                                              (64, 6, '上九', 'yang', '有孚于饮酒，无咎，濡其首，有孚失是。');
