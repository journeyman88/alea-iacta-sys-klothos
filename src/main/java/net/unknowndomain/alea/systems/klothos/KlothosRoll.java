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
import java.util.Set;
import net.unknowndomain.alea.dice.standard.D6;
import net.unknowndomain.alea.pools.DicePool;
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
    
    public KlothosRoll(Integer approach, Integer specLevel, KlothosModifiers ... mod)
    {
        this(approach, specLevel, Arrays.asList(mod));
    }
    
    public KlothosRoll(Integer approach, Integer specLevel, Collection<KlothosModifiers> mod)
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
    }
    
    @Override
    public GenericResult getResult()
    {
        List<Integer> actionRes = this.actionPool.getResults();
        KlothosResults results = buildResults(actionRes);
        results.setVerbose(mods.contains(KlothosModifiers.VERBOSE));
        return results;
    }
    
    private KlothosResults buildResults(List<Integer> actionRes)
    {
        actionRes.sort((Integer o1, Integer o2) ->
        {
            return -1 * o1.compareTo(o2);
        });
        Integer fate = null;
        if (mods.contains(KlothosModifiers.ENEMY_SPEC) || mods.contains(KlothosModifiers.CHAR_SPEC))
        {
            fate = D6.INSTANCE.roll();
        }
        KlothosResults results = new KlothosResults(actionRes, fate);
        int dice = actionRes.get(0);
        results.setTotal(approach + dice);
        if (dice == 1)
        {
            results.addMisfortune();
        }
        if (dice == 6)
        {
            results.addLuck();
        }
        if (mods.contains(KlothosModifiers.ENEMY_SPEC) && fate == 1)
        {
            results.addMisfortune();
        }
        if ((mods.contains(KlothosModifiers.CHAR_SPEC) && dice >= 5) || (mods.contains(KlothosModifiers.CHAR_MULTISPEC) && dice >= 4))
        {
            results.addLuck();
        }
        return results;
    }
}
