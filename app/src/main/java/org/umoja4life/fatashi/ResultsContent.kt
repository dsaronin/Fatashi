package org.umoja4life.fatashi

private const val DEBUG = true
private const val LOG_TAG = "ResultsContent"

data class ItemDetail(var entry: String, var def: String, var usage: String)

object ResultsContent {
// helper class for content

    /**
     * An array of kamusi result items from search
     */
    val RESULT_ITEMS: MutableList<ResultItem> = mutableListOf()

    private val sampleResult = listOf("aali  -- excellent\t__", "abadani  -- never\t__ kijana huyu hanywi pombe abadani.", "abiria aliyeketi ubavuni pangu  -- the passenger sitting at my side", "abiria  -- passenger", "-abiri  -- learn from experience\t__ kutokana na kifo cha wazee wake, ameabiri kwamba maisha ni magumu.", "-abiri  -- predict, foresee\t__ aliabiri kwamba mvua itanyesha leo.", "-abiri  -- travel, navigate, sail\t__ aliabiri kwenye mashua.", "-abudu  -- worship, idolize; heshimu, ogopa omba, nyenyekea, tumikia\t__ mila na itikadi pamoja na hayo imani ya kuabudu mizimu.", "abwe!  -- wow! (expl)\t__", "-acha  -- abandon, neglect\t__ kuku aliwaacha watoto wake.", "-acha  -- allow, permit, let sby do sth; ruhusu mtu afanye jambo\t__ wacha nitumie hadi jumatatu. alimwacha mtoto wake afanye atakavyo.", "-acha  -- avoid, cease, stop doing sth\t__ acha kucheza kamari.", "-acha  -- divorce, separation; tengana na mtu, kitu, jambo au hali\t__ amemwacha mkewe. kuku amewaacha watoto wake.", "-acha  -- give instructions, leave a message to sby while away; toa masharti au maagizo ya kutekelezwa na mtu mwingine wakati umeondoka\t__ aliacha maagizo kwa jirani zake.", "-acha  -- leave sth behind; bakiza kitu, mtu, mali\t__ aliacha mali nyingi aliipokufa. amekiacha chakula nusu sahani. nilimwacha fulani posta.", "-achama  -- gape, stare, gawk\t__ mbona unaachama?", "-acha mavanga", "-- acha udanganyifu au uongo (idm)\t__", "-acha mkono  -- die (idiom)\t__ alituacha mkono zamani.", "-achana  -- be separated\t__ kuachana naye", "-achana nasi  -- depart/leave us; die\t_", "acha!  -- neno la kuonyesha mshangao na furaha\t__ acha wee!", "-acha  -- pardon, release, let go\t__ nilimkamata mwizi wa mifuko na kisha nikamwachilia.", "-acha  -- pass by sth [directions]; pita kitu fulani\t__ acha nyumba ili mkono wa kushoto kisha uende moja kwa mjoa mpaka kwa hilo jengo. acha hoteli mkono wa kulia uende kama mitamia hivi.", "-acha  -- resign from sth; kutoendelea kujihusisha na kitu, shughuli, shirika fulani\t__ ameacha kazi.  achari  -- pickles\t__", "-achia  -- leave to sby\t__ aliniachia simu yake nimsajili katika mtandao wa duo.", "-achilia  -- acquit, discharge\t__ mahakama ilimwachilia huru mshtakiwa.", "-achisha  -- stop sby from doing sth\t__ achisha ziwa [wean a child]. achisha kazi [fire terminate sby employment].", "adabu  -- respect, good manners\t__", "ada  -- custom, tradition; habit\t__ ada za harusi. ana ada ya kuchelewa kazini kila siku. ada ya mja hunena, muungwana ni kitendo", "~a daima  -- lasting\t__ upendo wa daima; asipojua kusema daima hataelewana na wenzake.  adapta  -- adapter  (tech)", "ada ya pango  -- rent\t__ analipa shilingi ngapi ada ya pango?", "ada ya pango  -- rent (n)", "ada ya uhamisho  -- transfer fee\t__", "~a dayo  -- dial-up (adj)  (tech)", "adhabu  -- punishment, penalty; persecution\t__ mwanafunzi atapata adhabu kali. kibarua cha kuchuma majani chai ni adhabu.", "adha  -- inconvenience, nuisance, annoyance\t__ mbona hakunizaa katika ulimwengu usiokuwa na adha na ubaya?", "~a dhati  -- genuine, true\t__ mapenzi ya dhati kwa sana", "-adhibiwa  -- be punished\t__ ni haki mtu kuadhibiwa kwa jambo ambalo hakufanya?", "-adhibu  -- punish, harass\t__ serikali itawaadhibu wale wanavunja sheria. akiniadhibu basi mimi nitaripoti kwa wazee wake.", "-adhimisha  -- commemorate, celebrate, glorify, honor; sherekea jambo au siku kwa kuitukuza, ipa heshima\t__ nchi yetu iliadhimisha miaka thelathini ya kkupatikana kwa uhuru.", "adhimisho  (ma)  -- commemoration, celebration, observance\t__", "adhimu  -- bountiful, much\t__ alikuwa na mali adhimu", "adhimu  -- exalted, glory, prestigious, honorable\t__ kulikuwa na sherehe adhimu mtaani hivi karibuni.", "-adhiri, -aziri  -- disgrace, slander\t__ aliniaziri kwa kunidai pesa zake hadharani (in public).", "adhuhuri  -- noontime\t__ habari za adhuhuri?", "-adibu  -- teach good manners; fundisha mwenendo mwema au tabia njema; kuwa adabu\t__ mzee alimwadibu binti yake jinsi ya kuwashughulikia wageni.", "~adilifu  -- just and good; righteous\t__ kiongozi mwadilifu. daktari Nozi alikuwa kijana mwadilifu.", "adilifu  -- just and good; righteousness\t__ yote yalikuwa chini ya usimamizi mzuri na adilifu wa dada yake", "-adili  -- judge impartially; be impartial, be just\t__ mkuu wa wilaya anasifika sana katika kuadili shughuli mbalimbali za wilaya.")
    private val empty_query = "Enter a search query..."
    private val indexList: List<Int> = sampleResult.indices.toList()

    init {
        buildResultItems(
            empty_query,
            sampleResult
        )
   }

    // shuffleList is for debugging & testing purposes
    fun shuffleList(): List<String>  {
        val idx1 = indexList.random()
        var idx2 = (0..11).random() + idx1
        if (idx2 > indexList.size - 1 ) idx2 = indexList.size - 1
        return sampleResult.slice( idx1..idx2)
    }

    fun newQuery( s: String ) {
        buildResultItems(
            s,
            shuffleList()
        )
    }

    fun buildResultItems(query: String, resultList: List<String>)  {
        if ( RESULT_ITEMS.isNotEmpty() ) RESULT_ITEMS.clear()
//        RESULT_ITEMS.add(ResultItem("0", query))
        for ( i in resultList.indices ) RESULT_ITEMS.add(
            ResultItem(i.toString(), resultList[i])
        )
    }

    fun parseFields(position: Int) : ItemDetail {
        var entry = ""
        var def = ""
        var usage = ""

        return ItemDetail(entry, def, usage)
    }

    fun itemsCount() = RESULT_ITEMS.size

    /**
     * An item representing a piece of content.
     */
    data class ResultItem(val id: String, val content: String) {
        override fun toString(): String = content
    }
}

