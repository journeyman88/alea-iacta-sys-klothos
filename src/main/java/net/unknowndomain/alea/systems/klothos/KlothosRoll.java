/*
 * Copyright 2020 Marco Bignami.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.unknowndomain.alea.systems.klothos;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.unknowndomain.alea.random.SingleResult;
import net.unknowndomain.alea.random.SingleResultComparator;
import net.unknowndomain.alea.random.dice.DicePool;
import net.unknowndomain.alea.random.dice.bag.D6;
import net.unknowndomain.alea.roll.GenericResult;
import net.unknowndomain.alea.roll.GenericRoll;

/**
 *
 * @author journeyman
 */
public class KlothosRoll implements GenericRoll
{
    
    private final DicePool<D6> actionPool;
    private final int approach;
    private final Set<KlothosModifiers> mods;
    private final Locale lang;
    
    public KlothosRoll(Integer approach, Integer specLevel, Locale lang, KlothosModifiers ... mod)
    {
        this(approach, specLevel, lang, Arrays.asList(mod));
    }
    
    public KlothosRoll(Integer approach, Integer specLevel, Locale lang, Collection<KlothosModifiers> mod)
    {
        this.mods = new HashSet<>();
        if (mod != null)
        {
            this.mods.addAll(mod);
        }
        if (specLevel > 0)
        {
            this.mods.add(KlothosModifiers.CHAR_SPEC);
            if (specLevel > 1)
            {
                this.mods.add(KlothosModifiers.CHAR_MULTISPEC);
            }
        }
        int dice = 1;
        if (mods.contains(KlothosModifiers.BONDED_ALLY))
        {
            dice = 2;
        }
        this.actionPool = new DicePool<>(D6.INSTANCE, dice);
        this.approach = approach;
        this.lang = lang;
    }
    
    @Override
    public GenericResult getResult()
    {
        List<SingleResult<Integer>> actionRes = this.actionPool.getResults();
        KlothosResults results = buildResults(actionRes);
        results.setVerbose(mods.contains(KlothosModifiers.VERBOSE));
        return results;
    }
    
    private KlothosResults buildResults(List<SingleResult<Integer>> actionRes)
    {
        SingleResultComparator<Integer> comp = new SingleResultComparator<>(true);
        actionRes.sort(comp);
        SingleResult<Integer> fate = null;
        if (mods.contains(KlothosModifiers.ENEMY_SPEC) || mods.contains(KlothosModifiers.CHAR_SPEC))
        {
            fate = D6.INSTANCE.nextResult().get();
        }
        KlothosResults results = new KlothosResults(actionRes, fate);
        SingleResult<Integer> dice = actionRes.get(0);
        results.setTotal(approach + dice.getValue());
        if (dice.getValue() == 1)
        {
            results.addMisfortune();
        }
        if (dice.getValue() == 6)
        {
            results.addLuck();
        }
        if (mods.contains(KlothosModifiers.ENEMY_SPEC) && fate.getValue() == 1)
        {
            results.addMisfortune();
        }
        if ((mods.contains(KlothosModifiers.CHAR_SPEC) && dice.getValue() >= 5) || (mods.contains(KlothosModifiers.CHAR_MULTISPEC) && dice.getValue() >= 4))
        {
            results.addLuck();
        }
        results.setLang(lang);
        return results;
    }
}
