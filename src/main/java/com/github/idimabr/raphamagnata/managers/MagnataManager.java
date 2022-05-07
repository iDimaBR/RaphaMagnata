package com.github.idimabr.raphamagnata.managers;

import com.github.idimabr.raphamagnata.RaphaMagnata;
import com.github.idimabr.raphamagnata.objects.Account;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.*;
import java.util.stream.Collectors;

public class MagnataManager {

    private String magnata = "Nenhum";
    private List<String> richs = new ArrayList<>();

    public String getMagnata() {
        return magnata;
    }

    public void setMagnata(String magnata) {
        this.magnata = magnata;
    }

    public List<String> getRichs() {
        return richs;
    }

    public void setRichs(List<String> richs) {
        this.richs = richs;
    }

    public Map<String, Double> getTop(int startin, int stopin) {
        Map<String, Double> top = new LinkedHashMap<>(stopin);
        ArrayList<Account> players = new ArrayList<>();
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            Account account = new Account(player, RaphaMagnata.getInstance().getEconomyHook().getBalance(player));
            if (account.getMoney() == 0) continue;

            players.add(account);
        }

        players.sort(Account::compareTo);

        for (int i = startin; i < stopin; i++) {
            try {
                Account account = players.get(i);
                top.put(account.getPlayer(), account.getMoney());
            } catch (Exception e) {
                break;
            }
        }
        return top;
    }
}
