handle to user map

User:
handle
country
organization
rating

contest id to Contest map
Contest:
    contestId
    name
    type
    phase
    durationSeconds
    startTimeSeconds
    relativeTimeSeconds

problems [] : Problem
        index
        points
        rating
        name
rankRows [] : RankRow
    handle -> party.member[0].handle
    ghost -> party.ghost
    rank
    points
    problemResults[] : ProblemResult
        index
        points
        type



Internal API Response

ContestMeta:
    id
    name
    phase
    problems[] : Problems
        idx
        point
        name
        score

row[] : Row
    handle
    rating
    point
    country
    org
    problems[] : Problems
        idx
        point
        name
        score


Contest:


1. DTO Definition
2. API Response mapping
3. Persistence
4. DAO for storeUser(), getUser(), getContest(), storeContestResult()
5. Internal API - getContestResult(), getContestList()
6. Internal Cron job to storeContestResult(), getContestList()
7. Front End Table and Filter

resource: standing


Resources:
GET standingds/{contestId}?filter={country, org, pageId}