import random

faultDetectionTactics = ["ping/echo", "monitor", "heartbeat", "timestamp / seqeuence numbers", "condition monitoring", 
                         "sanity checking", "voting", "exception detection", "self-test"]

faultRecoveryTactics = ["replicated spare", "rollback", "exception handling", "silent software update", "retry", "ignore faulty behavior", 
                        "graceful degradation", "reconfiguration", "shadow", "state resynchronization", "escalating restart", "nonstop forwarding"]

faultPreventionTactics = ["removal from service", "transactions", "predictive model", "exception prevention", "increase competence set"]


allTactics = faultDetectionTactics + faultRecoveryTactics + faultPreventionTactics

chosenTactic = allTactics[random.randint(0, len(allTactics) - 1)]

print(chosenTactic)

tacticCategories = { "fault detection" : faultDetectionTactics, "fault recovery" : faultRecoveryTactics, "fault prevention" : faultPreventionTactics }

#for key in tacticCategories:
#    if tacticCategories[key] != None:
#        print("category:", key, "tactic:", tacticCategories[key])
