package org.umoja4life.fatashi

private const val DEBUG = true
private const val LOG_TAG = "ResultsContent"

// TODO: remove these before production
// fieldDelimiters.replace( rawContent, internalFields )
// private const val fieldDelimiters = "(\\s+--\\s+)|(\\t__[ \\t\\x0B\\f]+)".toRegex()
// private const val internalFields = "\t"

private val sampleResult = listOf("aali\texcellent\t", "abadani\tnever\tkijana huyu hanywi pombe abadani.", "abiria aliyeketi ubavuni pangu\tthe passenger sitting at my side", "abiria\tpassenger", "-abiri\tlearn from experience\tkutokana na kifo cha wazee wake, ameabiri kwamba maisha ni magumu.", "-abiri\tpredict, foresee\taliabiri kwamba mvua itanyesha leo.", "-abiri\ttravel, navigate, sail\taliabiri kwenye mashua.", "-abudu\tworship, idolize; heshimu, ogopa omba, nyenyekea, tumikia\tmila na itikadi pamoja na hayo imani ya kuabudu mizimu.", "abwe!\twow! (expl)\t", "-acha\tabandon, neglect\tkuku aliwaacha watoto wake.", "-acha\tallow, permit, let sby do sth; ruhusu mtu afanye jambo\twacha nitumie hadi jumatatu. alimwacha mtoto wake afanye atakavyo.", "-acha\tavoid, cease, stop doing sth\tacha kucheza kamari.", "-acha\tdivorce, separation; tengana na mtu, kitu, jambo au hali\tamemwacha mkewe. kuku amewaacha watoto wake.", "-acha\tgive instructions, leave a message to sby while away; toa masharti au maagizo ya kutekelezwa na mtu mwingine wakati umeondoka\taliacha maagizo kwa jirani zake.", "-acha\tleave sth behind; bakiza kitu, mtu, mali\taliacha mali nyingi aliipokufa. amekiacha chakula nusu sahani. nilimwacha fulani posta.", "-achama\tgape, stare, gawk\tmbona unaachama?", "-acha mavanga\tacha udanganyifu au uongo (idm)", "-acha mkono\tdie (idiom)\talituacha mkono zamani.", "-achana\tbe separated\tkuachana naye", "-achana nasi\tdepart/leave us; die\t_", "acha!\tneno la kuonyesha mshangao na furaha\tacha wee!", "-acha\tpardon, release, let go\tnilimkamata mwizi wa mifuko na kisha nikamwachilia.", "-acha\tpass by sth [directions]; pita kitu fulani\tacha nyumba ili mkono wa kushoto kisha uende moja kwa mjoa mpaka kwa hilo jengo. acha hoteli mkono wa kulia uende kama mitamia hivi.", "-acha\tresign from sth; kutoendelea kujihusisha na kitu, shughuli, shirika fulani\tameacha kazi.", "achari\tpickles", "-achia\tleave to sby\taliniachia simu yake nimsajili katika mtandao wa duo.", "-achilia\tacquit, discharge\tmahakama ilimwachilia huru mshtakiwa.", "-achisha\tstop sby from doing sth\tachisha ziwa [wean a child]. achisha kazi [fire terminate sby employment].", "adabu\trespect, good manners\t", "ada\tcustom, tradition; habit\tada za harusi. ana ada ya kuchelewa kazini kila siku. ada ya mja hunena, muungwana ni kitendo", "~a daima\tlasting\tupendo wa daima; asipojua kusema daima hataelewana na wenzake.", "adapta\tadapter  (tech)", "ada ya pango\trent\tanalipa shilingi ngapi ada ya pango?", "ada ya pango\trent (n)", "ada ya uhamisho\ttransfer fee\t", "~a dayo\tdial-up (adj)  (tech)", "adhabu\tpunishment, penalty; persecution\tmwanafunzi atapata adhabu kali. kibarua cha kuchuma majani chai ni adhabu.", "adha\tinconvenience, nuisance, annoyance\tmbona hakunizaa katika ulimwengu usiokuwa na adha na ubaya?", "~a dhati\tgenuine, true\tmapenzi ya dhati kwa sana", "-adhibiwa\tbe punished\tni haki mtu kuadhibiwa kwa jambo ambalo hakufanya?", "-adhibu\tpunish, harass\tserikali itawaadhibu wale wanavunja sheria. akiniadhibu basi mimi nitaripoti kwa wazee wake.", "-adhimisha\tcommemorate, celebrate, glorify, honor; sherekea jambo au siku kwa kuitukuza, ipa heshima\tnchi yetu iliadhimisha miaka thelathini ya kkupatikana kwa uhuru.", "adhimisho  (ma)\tcommemoration, celebration, observance\t", "adhimu\tbountiful, much\talikuwa na mali adhimu", "adhimu\texalted, glory, prestigious, honorable\tkulikuwa na sherehe adhimu mtaani hivi karibuni.", "-adhiri, -aziri\tdisgrace, slander\taliniaziri kwa kunidai pesa zake hadharani (in public).", "adhuhuri\tnoontime\thabari za adhuhuri?", "-adibu\tteach good manners; fundisha mwenendo mwema au tabia njema; kuwa adabu\tmzee alimwadibu binti yake jinsi ya kuwashughulikia wageni.", "~adilifu\tjust and good; righteous\tkiongozi mwadilifu. daktari Nozi alikuwa kijana mwadilifu.", "adilifu\tjust and good; righteousness\tyote yalikuwa chini ya usimamizi mzuri na adilifu wa dada yake", "-adili\tjudge impartially; be impartial, be just\tmkuu wa wilaya anasifika sana katika kuadili shughuli mbalimbali za wilaya.")
private val empty_query = "Enter a search query..."
private val indexList: List<Int> = sampleResult.indices.toList()

data class ItemDetail(var entry: String, var def: String, var usage: String)

// "model" for dealing with results data

object ResultsContent {

    // An array of kamusi result items from search
    val RESULT_ITEMS: MutableList<ResultItem> = mutableListOf()

    init {
        buildResultItems( sampleResult)  // REEVALUATE and maybe remove from initial showing
   }

    // shuffleList is for debugging & testing purposes
    fun shuffleList(): List<String>  {
        val idx1 = indexList.random()
        var idx2 = (0..11).random() + idx1
        if (idx2 > indexList.size - 1 ) idx2 = indexList.size - 1
        return sampleResult.slice( idx1..idx2)
    }

    fun newQuery( s: String ) = buildResultItems( shuffleList() )

    fun itemsCount() = RESULT_ITEMS.size

    // buildResultItems -- main entry point for displaying a new list of results
    // which have come from the backend, via AndroidPlatform.listOut()
    fun buildResultItems(resultList: List<String>)  {
        if ( RESULT_ITEMS.isNotEmpty() ) RESULT_ITEMS.clear()

        for ( i in resultList.indices ) {
            val (entry, definition, usage) = parseFields(resultList[i])
            RESULT_ITEMS.add(
                ResultItem(i, resultList[i], entry,  definition, usage)
            )
        }
    }

    // TODO: move this into the project configuration file
    private val itemRegex = "^([^\\t]+)\\t([^\\t]+)\\t?([^\\t]*)\$"

    // parseFields  -- split a search result line into three fields
    // TODO: needs to be taken over by Kamusi Class functions

    fun parseFields(rawItem: String) : ItemDetail {
        val keyfrag = itemRegex.toRegex().find(rawItem)

            // if there was an issue matching/splitting to fields, at least
            // return the entire search result line as the entry default!
        return ItemDetail(
            keyfrag?.groupValues?.get(1) ?: rawItem,
            keyfrag?.groupValues?.get(2) ?: "",
            keyfrag?.groupValues?.get(3) ?: ""
        )
    }

    // An item representing a piece of content.
    data class ResultItem(
        val position: Int,
        val content: String,
        val entry: String,
        val definition: String,
        val usage: String
    ) {
        override fun toString(): String = content
    }
}