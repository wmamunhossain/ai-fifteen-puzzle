#include <sstream>
#include <queue>
#include <stack>
#include <set>
#include <map>
#include <cstdio>
#include <cstdlib>
#include <cctype>
#include <complex>
#include <cmath>
#include <iostream>
#include <iomanip>
#include <string>
#include <utility>
#include <vector>
#include <algorithm>
#include <bitset>
#include <list>
#include <string.h>
#include <assert.h>
#include <time.h>

using namespace std;

#define SZ(x) ((int)x.size())
#define all(a) a.begin(),a.end()
#define allr(a) a.rbegin(),a.rend()
#define clrall(name,val) memset(name,(val),sizeof(name));
#define EPS 1e-9
#define ll long long
#define ull long long unsigned
#define SF scanf
#define PF printf
#define sf scanf
#define pf printf
#define psb(b) push_back((b))
#define ppb() pop_back()
#define oo (1<<28)
#define mp make_pair
#define mt make_tuple
#define get(a,b) get<b>(a)
#define fs first
#define sc second
#define x first
#define y second
#define Read freopen("in.txt","r",stdin)
#define Write freopen("out2.txt","w",stdout)
#define __ std::ios_base::sync_with_stdio (false),cin.tie(0),cout.tie(0)

ll MulModL(ll B,ll P,ll M) {    ll R=0; while(P>0)      { if((P&1ll)==1) { R=(R+B); if(R>=M) R-=M; } P>>=1ll; B=(B+B); if(B>=M) B-=M; } return R; }

ll MulModD(ll B, ll P,ll M) {   ll I=((long double)B*(long double)P/(long double)M);    ll R=B*P-M*I; R=(R%M+M)%M;  return R; }

ll BigMod(ll B,ll P,ll M) {     ll R=1; while(P>0)      { if(P%2==1) { R=(R*B)%M; } P/=2; B=(B*B)%M; } return R; } /// (B^P)%M

template<class T1> void deb(T1 e1){cout<<e1<<"\n";}
template<class T1,class T2> void deb(T1 e1,T2 e2){cout<<e1<<" "<<e2<<"\n";}
template<class T1,class T2,class T3> void deb(T1 e1,T2 e2,T3 e3){cout<<e1<<" "<<e2<<" "<<e3<<"\n";}
template<class T1,class T2,class T3,class T4> void deb(T1 e1,T2 e2,T3 e3,T4 e4){cout<<e1<<" "<<e2<<" "<<e3<<" "<<e4<<"\n";}
template<class T1,class T2,class T3,class T4,class T5> void deb(T1 e1,T2 e2,T3 e3,T4 e4,T5 e5){cout<<e1<<" "<<e2<<" "<<e3<<" "<<e4<<" "<<e5<<"\n";}
template<class T1,class T2,class T3,class T4,class T5,class T6> void deb(T1 e1,T2 e2,T3 e3,T4 e4,T5 e5,T6 e6){cout<<e1<<" "<<e2<<" "<<e3<<" "<<e4<<" "<<e5<<" "<<e6<<"\n";}
template<class T1,class T2,class T3,class T4,class T5,class T6,class T7> void deb(T1 e1,T2 e2,T3 e3,T4 e4,T5 e5,T6 e6,T7 e7){cout<<e1<<" "<<e2<<" "<<e3<<" "<<e4<<" "<<e5<<" "<<e6<<" "<<e7<<"\n";}

//int dx[]= {-1,-1,0,0,1,1};
//int dy[]= {-1,0,-1,1,0,1};
int dx[]= {0,0,1,-1};/*4 side move*/
int dy[]= {-1,1,0,0};/*4 side move*/
//int dx[]= {1,1,0,-1,-1,-1,0,1};/*8 side move*/
//int dy[]= {0,1,1,1,0,-1,-1,-1};/*8 side move*/
//int dx[]={1,1,2,2,-1,-1,-2,-2};/*night move*/
//int dy[]={2,-2,1,-1,2,-2,1,-1};/*night move*/

typedef vector<vector<int> > vvi;
typedef pair<int,int> pii;

map<vvi,int> myMap;
map<vvi,vvi> parMap;

const int blankVal = 16;

vvi base(4,vector<int>(4));

struct node
{
    vvi board;
    int cost;
    int depth;
    node() {}
    node(vvi board,int cost,int depth):board(board),cost(cost),depth(depth) {}
    bool operator < (const node &x) const
    {
        return cost>x.cost;
    }
};

pii getBlank(vvi curBoard)
{
    for(int i=0;i<4;i++)
    {
        for(int j=0;j<4;j++)
        {
            if(curBoard[i][j]==blankVal)
            {
                return mp(i,j);
            }
        }
    }
    return mp(-1,-1);
}

int manHattan(vvi board)
{
    int cost=0,x,y,v;
    for(int i=0;i<4;i++)
    {
        for(int j=0;j<4;j++)
        {
            v=board[i][j];
            x=(v-1)>>2;
            y=(v-1)&3;
            cost+=abs(x-i)+abs(y-j);
        }
    }
    return cost;
}

void init()
{
    myMap.clear();
    parMap.clear();
}

void printMat(vvi myMat)
{
    for(int i=0;i<4;i++)
    {
        for(int j=0;j<4;j++)
        {
            if(j) pf(" ");
            pf("%2d",myMat[i][j]);
        }
        puts("");
    }
    return ;
}

pii aStar(vvi curBoard)
{
    init();
    pii blank,nblank;
    priority_queue<node> Q;
    int fcost=manHattan(curBoard),hcost,ndepth;
    vvi nextBoard,sBoard=curBoard;
    node curNode,nextNode;
    Q.push(node(curBoard,fcost,0));
    myMap[curBoard]=fcost;
    parMap[curBoard]=curBoard;
    while(!Q.empty())
    {
        curNode=Q.top();    Q.pop();
        curBoard=curNode.board;
        if(curNode.depth>40) continue;
        if(myMap[curBoard]<curNode.cost) continue;
//        //deb("cur cost",curNode.cost,SZ(Q));
        hcost=manHattan(curBoard);
        if(hcost==0) break;
        blank=getBlank(curBoard);
        nextBoard=curBoard;
        for(int i=0;i<4;i++)
        {
            nblank=mp(blank.fs+dx[i],blank.sc+dy[i]);
            if(nblank.x>=0&&nblank.x<4&&nblank.y>=0&&nblank.y<4)
            {
                swap(nextBoard[nblank.x][nblank.y],nextBoard[blank.x][blank.y]);
                hcost=manHattan(nextBoard);
                ndepth=curNode.depth+1;
                fcost=hcost+ndepth;
                if(myMap.count(nextBoard)==0||myMap[nextBoard]>fcost)
                {
                    Q.push(node(nextBoard,fcost,ndepth));
//                    //deb("Source::::::::::::::::::::::::");
//                    printMat(curBoard);
//                    //deb("::::::::::::::::::::::::::::::");
//                    //deb("Destination||||||||||||||||||||||");
//                    printMat(nextBoard);
//                    //deb("|||||||||||||||||||||||||||||||||");
//                    //deb("*****",hcost,ndepth);
//                    getchar();
                    parMap[nextBoard]=curBoard;
                    myMap[nextBoard]=fcost;
                }
                swap(nextBoard[nblank.x][nblank.y],nextBoard[blank.x][blank.y]);
            }
        }
    }
//    //deb("***************END****************");
    curBoard=base;
    if(parMap.count(base)==0)
    {
        //deb("LOL");
    }
    while(parMap[curBoard]!=sBoard)
    {
//        //deb(":::::::::::::::::::::");
//        printMat(curBoard);
//        //deb(":::::::::::::::::::::");
        curBoard=parMap[curBoard];
//        //deb("*********************");
//        printMat(curBoard);
//        //deb("*********************");
    }
    return getBlank(curBoard);
}

void copyMat(vvi &mat)
{
    for(int i=0;i<4;i++)
    {
        for(int j=0;j<4;j++)
        {
            mat[i][j]=base[i][j];
        }
    }
    return ;
}

void randomMove(vvi &mat)
{
    int mov=50;
    int k;
    pii blank = getBlank(mat);
    int x=blank.x,y=blank.y;
    int px=x,py=y;
    while(mov)
    {
        k=rand()&3;
        px=x; py=y;
        x+=dx[k]; y+=dy[k];
        if(x>=0&&x<4&&y>=0&&y<4)
        {
            mov--;
            swap(mat[px][py],mat[x][y]);
        }
        else
        {
            x=px;
            y=py;
        }
    }
    return ;
}
int main()
{
    freopen("board.txt","r",stdin);
    freopen("next.txt","w",stdout);

//    cout<<"CALLED"<<endl;
    srand(time(NULL));
    for(int i=0,k=1;i<4;i++)
    {
        for(int j=0;j<4;j++)
        {
            base[i][j]=k++;
        }
    }
    vvi myMat(4,vector<int>(4));

//    copyMat(myMat);
//    randomMove(myMat);
    for(int i=0;i<4;++i)
    {
        for(int j=0;j<4;++j)
        {
            cin>>myMat[i][j];
        }
    }

//    printMat(myMat);
    pii blank,nblank;
    int tot=0;
//    while(myMat!=base)
//    {
        blank = getBlank(myMat);
        nblank = aStar(myMat);
        swap(myMat[blank.x][blank.y],myMat[nblank.x][nblank.y]);

        cout<<nblank.first<<" "<<nblank.second<<endl;

        //deb("::::::::::::::Step:::::::::::::::");
//        printMat(myMat);
        //deb("::::::::::::::Step:::::::::::::::");
        if(myMat==base)
        {
            //deb("::::::::::::Solved::::::::::::::");
            //deb("Total Moves : ",tot);
//            break;
        }
        tot++;
//    }
            exit(0);
    return 0;
}














